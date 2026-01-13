package collections

object Practice extends App{



  val array = Array(1,2,3)
  array.update(2,10)
  array.foreach(println)


  //Ways to create functions
  val add = (x:Int) => x + 10 //anonymous function

  val multiple:(Int,Int) => Int = (x,y) => x*y //named function
  val single: Int => Int=x => x+1

  def square(x: Int): Int = x * 10 //method to function
  val f= square _


  val divide:Int=>Int=>Double= x => y => x.toDouble/y

  divide(10)(20)

}

