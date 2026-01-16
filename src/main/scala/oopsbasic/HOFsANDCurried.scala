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
    else (x:Int) => nTimesBetter(f,n-1)(f(x))
  }

  val plus10 = nTimesBetter(incrementer,10)
  println(plus10(1))



  //curried example
  def curriedFormater(c:String)(x:Double):String = c.format(x)

  val standardFormatter:Double => String = curriedFormater("%.2f")
  val preciseFormat:Double=>String = curriedFormater("%.8f")

  println(standardFormatter(Math.PI))
  println(preciseFormat(Math.PI))

  //About format method in scala
  val s="I am %s and I am %d years old".format("alice",25)
  println(s)

  /****
   *  -  %s - string
   *  -  %d - number
   *  -  %f - double
   *  -  %b - boolean
   *  -  %c - char
   *
   */
  
}
