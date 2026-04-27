package zomatoordersproject.service

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import zomatoordersproject.domain._
import zomatoordersproject.repository.OrderRepository

import scala.concurrent.Future
import akka.pattern.CircuitBreakerOpenException

class CircuitBreakerSpec extends AsyncWordSpec with Matchers {
  val testKit = ActorTestKit()
  implicit val typedSystem: ActorSystem[_] = testKit.system
  
  // A repository that constantly fails to trigger circuit breaker
  val failingRepo = new OrderRepository {
    override def save(order: Order): Future[Order] = Future.failed(new RuntimeException("DB Down"))
    override def findById(id: String): Future[Option[Order]] = Future.failed(new RuntimeException("DB Down"))
    override def findAll(): Future[List[Order]] = Future.failed(new RuntimeException("DB Down"))
    override def updateStatus(id: String, status: OrderStatus): Future[Option[Order]] = Future.failed(new RuntimeException("DB Down"))
    override def delete(id: String): Future[Boolean] = Future.failed(new RuntimeException("DB Down"))
  }

  "OrderService CircuitBreaker" should {
    "open after repeated failures and reject requests fast" in {
      val guardian = testKit.spawn(zomatoordersproject.actors.OrderGuardian())
      val (queue, _) = zomatoordersproject.stream.OrderStream.createStream(typedSystem)
      val service = new OrderService(guardian, failingRepo, queue)(typedSystem, typedSystem.executionContext)

      // Cause 5 failures (the maxFailures limit configured in OrderService)
      val results = Future.sequence((1 to 5).map(_ => service.getAllOrders().recover { case _ => List.empty }))
      
      results.flatMap { _ =>
        // The 6th request should fail with CircuitBreakerOpenException immediately without hitting the repo
        recoverToExceptionIf[CircuitBreakerOpenException] {
          service.getAllOrders()
        }.map { ex =>
          ex shouldBe a[CircuitBreakerOpenException]
        }
      }
    }
  }
}
