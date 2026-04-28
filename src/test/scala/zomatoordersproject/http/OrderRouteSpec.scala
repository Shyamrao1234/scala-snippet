package zomatoordersproject.http

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import zomatoordersproject.domain._
import zomatoordersproject.json.JsonSupport
import zomatoordersproject.service.OrderService
import zomatoordersproject.actors.OrderGuardian
import zomatoordersproject.repository.InMemoryOrderRepository

class OrderRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with JsonSupport {

  val testKit = ActorTestKit()
  implicit val typedSystem: ActorSystem[_] = testKit.system

  val guardian = testKit.spawn(OrderGuardian())
  val repository = new InMemoryOrderRepository()(typedSystem.executionContext)
  val (queue, _) = zomatoordersproject.stream.OrderStream.createStream(typedSystem)
  
  val orderService = new OrderService(guardian, repository, queue)(typedSystem, typedSystem.executionContext)
  val routes = new OrderRoutes(orderService).routes

  "OrderRoutes" should {
    "create an order successfully (POST /orders)" in {
      val req = CreateOrderRequest("order-1", "user-1", List("item-1", "item-2"))
      Post("/orders", req) ~> routes ~> check {
        status shouldBe StatusCodes.Created
        val resp = responseAs[Order]
        resp.orderId shouldBe "order-1"
        resp.status shouldBe OrderStatus.Pending
      }
    }

    "get an order by id (GET /orders/{id})" in {
      Get("/orders/order-1") ~> routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Order].orderId shouldBe "order-1"
      }
    }

    "return 404 for missing order" in {
      Get("/orders/order-missing") ~> routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "update order status (PUT /orders/update/{id})" in {
      val req = UpdateStatusRequest("PREPARING")
      Put("/orders/update/order-1", req) ~> routes ~> check {
        status shouldBe StatusCodes.OK
        val resp = responseAs[Order]
        resp.status shouldBe OrderStatus.Preparing
      }
    }
  }

  override def afterAll(): Unit = {
    testKit.shutdownTestKit()
  }
}
