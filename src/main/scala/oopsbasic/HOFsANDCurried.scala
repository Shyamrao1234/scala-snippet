package oopsbasic


object HOFsANDCurried extends App {


  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))
  }

  val incrementer: Int => Int = _ + 1
  println(nTimes(incrementer, 10, 1))


  def nTimesBetter(f: Int => Int, n: Int): Int => Int = {
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))
  }

  val plus10 = nTimesBetter(incrementer, 10)
  println(plus10(1))


  //curried example
  def curriedFormater(c: String)(x: Double): String = c.format(x)

  val standardFormatter: Double => String = curriedFormater("%.2f")
  val preciseFormat: Double => String = curriedFormater("%.8f")

  println(standardFormatter(Math.PI))
  println(preciseFormat(Math.PI))

  //About format method in scala
  val s = "I am %s and I am %d years old".format("alice", 25)
  println(s)

  /** **
   *  - %s - string
   *  - %d - number
   *  - %f - double
   *  - %b - boolean
   *  - %c - char
   *
   */


  /** *
   * Higher order functions exercise
   */
  def applyOperations(f: (Int, Int) => Int, x: Int, y: Int): Int = {
    f(x, y)
  }

  val adder: (Int, Int) => Int = _ + _
  val multiply: (Int, Int) => Int = _ * _

  println(applyOperations(adder, 2, 3))
  println(applyOperations(multiply, 2, 3))


}

object HigherOrderExample extends App {


  def applyDiscount(price: Double, discountStrategy: Double => Double) = {
    discountStrategy(price)
  }

  val festiveDiscount: Double => Double = price => price * 0.8
  val memberShipDiscount: Double => Double = price => price * 0.9

  val originalPrice = 1000.0

  println(applyDiscount(originalPrice, festiveDiscount))
  println(applyDiscount(originalPrice, memberShipDiscount))


}


class Person(name: String) {

  val p=new Person("shyam")

}