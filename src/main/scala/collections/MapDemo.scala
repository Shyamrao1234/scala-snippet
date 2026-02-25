package collections

import scala.collection.mutable

object MapDemo extends App {

  /**
   * order not maintain
   *
   */
  val map = mutable.HashMap[String, Any]()
  map(null) = 10
  map("A") = 10
  map("B") = 20

  /**
   *  order is maintained here
   */

  val linkedHashMap=mutable.LinkedHashMap[String,Any]()

  linkedHashMap("a") = 10
  linkedHashMap("b") = 20
  linkedHashMap("c") = 30

  println(linkedHashMap.head)
  val str= "*"

  println(str(0).isLetterOrDigit)
}
