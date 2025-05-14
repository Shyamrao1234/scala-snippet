/**
  * Created by $CapName on May 14, 2025.
  */

object ScalaBasicCode extends App {

  /**
    * scala function snippet
    */
  val add = (x: Int) => x + 1

  val add1 = () => 15

  /**
    * scala methods snippet
    */
  def greeting(x: Int): Int = {
    x + 10
  }




  val greetingFunc: Int => Int = greeting _

  println(greetingFunc(10))


  def great: Int = {
    10
  }

  val greatFunc: () => Int = great _

  println(greatFunc())

}
