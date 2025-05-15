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
object Point1 extends App{
  val p1=new Point1
  p1.setX(20)

}


object GetterNSetter extends App{

  val p=new Point
  p.x = 20
  println(p.x)


}
