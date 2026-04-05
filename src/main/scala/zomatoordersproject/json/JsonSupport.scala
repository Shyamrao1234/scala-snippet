package zomatoordersproject.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import zomatoordersproject.model._
/**
 * Created by Shyamrao on Mar 07, 2026.
 */

trait JsonSupport extends  DefaultJsonProtocol with SprayJsonSupport{

  implicit  val orderFormat= jsonFormat4(Order)

}
