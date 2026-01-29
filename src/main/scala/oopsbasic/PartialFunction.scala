package oopsbasic

object PartialFunction extends App {


  // Main use of partial function is to check and return the data
  val pf: PartialFunction[Int, String] = {
    case 1 => "one"
    case 2 => "two"
  }

  println(pf.isDefinedAt(2))
  println(pf(1))

}
