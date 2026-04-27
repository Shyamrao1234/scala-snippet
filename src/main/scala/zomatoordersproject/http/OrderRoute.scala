package zomatoordersproject.http

import akka.actor.typed._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.util.Timeout
import zomatoordersproject.json._
import zomatoordersproject.domain._
import zomatoordersproject.service.OrderService
import scala.util.{Success, Failure}
import scala.concurrent.duration._


class OrderRoutes(orderService: OrderService)
  extends JsonSupport {

  implicit val timeout: Timeout = Timeout(3.seconds)

  val routes =
    pathPrefix("orders") {
      pathEnd {
        post {
          entity(as[CreateOrderRequest]) { req =>
            val order = Order.create(req.orderId, req.userId, req.items)
            onComplete(orderService.createOrder(order)) {
              case Success(created) => complete(StatusCodes.Created, created)
              case Failure(ex) => complete(StatusCodes.InternalServerError, ApiError("CREATE_FAILED", ex.getMessage))
            }
          }
        }
      } ~ path(Segment) { id =>
        get {
          onComplete(orderService.getOrderById(id)) {
            case Success(Some(order)) => complete(order)
            case Success(None) => complete(StatusCodes.NotFound, ApiError("NOT_FOUND", "Order not found"))
            case Failure(ex) => complete(StatusCodes.InternalServerError, ApiError("GET_FAILED", ex.getMessage))
          }
        }
      } ~ path("all") {
        get {
          onComplete(orderService.getAllOrders()) {
            case Success(orders) => complete(orders)
            case Failure(ex) => complete(StatusCodes.InternalServerError, ApiError("GET_ALL_FAILED", ex.getMessage))
          }
        }
      } ~ path("update" / Segment) { id =>
        put {
          entity(as[UpdateStatusRequest]) { req =>
            OrderStatus.fromString(req.status) match {
              case Right(status) =>
                onComplete(orderService.updateStatus(id, status)) {
                  case Success(updated) => complete(updated)
                  case Failure(ex) => complete(StatusCodes.InternalServerError, ApiError("UPDATE_FAILED", ex.getMessage))
                }
              case Left(err) => complete(StatusCodes.BadRequest, ApiError("INVALID_STATUS", err))
            }
          }
        }
      }
    } ~
      pathSingleSlash {
        getFromResource("public/index.html")
      } ~
      getFromResourceDirectory("public")
}