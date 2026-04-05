package zomatoordersproject.http

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

import akka.actor.typed._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import zomatoordersproject.json._
import zomatoordersproject.model._
import zomatoordersproject.service.OrderService
import scala.concurrent.duration._


class OrderRoutes(orderService: OrderService)
  extends JsonSupport {

  implicit val timeout: Timeout = Timeout(3.seconds)

  val routes =
    pathPrefix("orders") {
      pathEnd {
        post {
          entity(as[Order]) { order =>
            orderService.createOrder(order)
            complete("Order created")
          }
        }
      } ~ path(Segment) { id =>
        get {
          complete(orderService.getOrderById(id))
        }
      } ~ path("all") {
        get {
          complete(orderService.getAllOrders())
        }
      } ~ path("update" / Segment / Segment) { (id, status) =>
        put {
          orderService.updateStatus(id, status)
          complete("Successfully updated")
        }
      }
    }

}
