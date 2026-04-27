package zomatoordersproject.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import zomatoordersproject.domain._
import java.time.Instant
import scala.util.Try

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

trait JsonSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object InstantFormat extends RootJsonFormat[Instant] {
    def write(instant: Instant): JsValue = JsString(instant.toString)
    def read(value: JsValue): Instant = value match {
      case JsString(str) => Try(Instant.parse(str)).getOrElse(deserializationError(s"Invalid Instant format: $str"))
      case _ => deserializationError("Expected string for Instant")
    }
  }

  implicit object OrderStatusFormat extends RootJsonFormat[OrderStatus] {
    def write(status: OrderStatus): JsValue = JsString(status.label)
    def read(value: JsValue): OrderStatus = value match {
      case JsString(str) => OrderStatus.fromString(str).getOrElse(deserializationError(s"Invalid OrderStatus: $str"))
      case _ => deserializationError("Expected string for OrderStatus")
    }
  }

  implicit val orderFormat = jsonFormat6(Order.apply)
  implicit val createOrderRequestFormat = jsonFormat3(CreateOrderRequest)
  implicit val updateStatusRequestFormat = jsonFormat1(UpdateStatusRequest)
  implicit val apiErrorFormat = jsonFormat2(ApiError)

}
