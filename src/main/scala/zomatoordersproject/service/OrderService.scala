package zomatoordersproject.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import zomatoordersproject.actors.OrderGuardian
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import zomatoordersproject.domain.{Order, OrderStatus}
import zomatoordersproject.repository.OrderRepository
import akka.pattern.CircuitBreaker

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

/**
 * Created by Shyamrao on Mar 14, 2026.
 */

class OrderService(
  orderActor: ActorRef[OrderGuardian.Command],
  orderRepository: OrderRepository,
  eventQueue: akka.stream.scaladsl.SourceQueueWithComplete[Order]
)(implicit system: ActorSystem[_], ec: ExecutionContext) {

  implicit val timeout: Timeout = Timeout(5.seconds)

  val circuitBreaker = new CircuitBreaker(
    system.toClassic.scheduler,
    maxFailures = 5,
    callTimeout = 3.seconds,
    resetTimeout = 1.minute
  )

  def createOrder(order: Order): Future[Order] = {
    val actorRes = orderActor.ask[Order](replyTo => OrderGuardian.CreateOrder(order, replyTo))
    actorRes.flatMap { savedOrder => 
      circuitBreaker.withCircuitBreaker(orderRepository.save(savedOrder)).map { res =>
        eventQueue.offer(res)
        res
      }
    }
  }

  def getOrderById(orderId: String): Future[Option[Order]] = {
    // Read from repo for high scalability
    circuitBreaker.withCircuitBreaker(orderRepository.findById(orderId))
  }

  def getAllOrders(): Future[List[Order]] = {
    circuitBreaker.withCircuitBreaker(orderRepository.findAll())
  }

  def updateStatus(orderId: String, status: OrderStatus): Future[Order] = {
    val actorRes = orderActor.ask[Order](replyTo => OrderGuardian.UpdateStatus(orderId, status, replyTo))
    actorRes.flatMap { updatedOrder => 
      circuitBreaker.withCircuitBreaker(
        orderRepository.updateStatus(orderId, status).map(_.getOrElse(updatedOrder))
      ).map { res =>
        eventQueue.offer(res)
        res
      }
    }
  }

}
