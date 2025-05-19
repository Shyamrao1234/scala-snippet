package scalabasics

import scala.beans.BeanProperty

/**
 * Created by $CapName on May 15, 2025.
 */
class Point {
  private var _x = 0

  def x = _x

  def x_=(value: Int) = {
    _x = value
  }

}

class Point1 {
  @BeanProperty var x: Int = 0
}

object Point1 extends App {
  val p1 = new Point1
  p1.setX(20)

}


object GetterNSetter extends App {

  val p = new Point
  p.x = 20
  println(p.x)


}


object OverloadedMethod extends App {

  //    def func(a: Int = 10) = {
  //      a
  //    }
  //
  //    def func(b: String = "B") = {
  //      b
  //    }
  //
  //    println(func())


  //  def func(a: Int) = {
  //    println("func A")
  //    a + 10
  //  }
  //
  //  def func(b: Int, x: String = "A") = {
  //    println("func B")
  //    b + 20
  //  }
  //
  //  println(func(10))


  def demo(a: => Int) = {
    for (i <- 1 to 10) {
      println(a)
    }
  }

  var y = 0
  demo {
    y += 1
    y
  }


}