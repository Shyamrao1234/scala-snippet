package collections

object MapDemo extends App {


  val map = scala.collection.mutable.Map(1 -> "A", 2 -> "B")

  map.put(1,"C") match {
    case None => println("None")
    case Some(value) => ""
  }



}
