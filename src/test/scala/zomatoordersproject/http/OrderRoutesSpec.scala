package zomatoordersproject.http

import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import zomatoordersproject.actors.OrderGuardian
import zomatoordersproject.json.JsonSupport
import zomatoordersproject.model.Order
import zomatoordersproject.service.OrderService

/**
 * Created by Shyamrao on Mar 20, 2026.
 */

class OrderRoutesSpec extends AnyWordSpecLike
  with Matchers
  with ScalatestRouteTest
  with JsonSupport {


  val typedSystem = system.toTyped
  implicit val ec        = typedSystem.executionContext
  implicit val scheduler = typedSystem.scheduler
  val actor        = typedSystem.systemActorOf(OrderGuardian(), "OrderGuardian")
  val orderService = new OrderService(actor)
  val route        = new OrderRoutes(orderService)


  "Order API " should {
    "create an order" in {
      val order = Order("1", List("MackBook"), "User-1", "CREATED")
      Post("/orders", order) ~> route.routes ~> check {
        Thread.sleep(1000)
        status.intValue() shouldBe 200
      }
    }

    "Create an order and get the same order" in {
      val order = Order("1", List("Laptop"), "User-1", "CREATED")

      Post("/orders", order) ~> route.routes ~> check {
        status.isSuccess() shouldBe true
      }

      Get("/orders/1") ~> route.routes ~> check {
        val response = responseAs[Option[Order]]
        response.get.orderId shouldBe "1"
      }
    }





  }


}
