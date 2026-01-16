package oopsbasic

object Functions extends App{


  val doubler=new MyFunction[Int,Int] {
    override def apply(x: Int): Int = x+x
  }
  println(doubler(20))


  val stringToInt=new Function[String,Int] {
    override def apply(v1: String): Int = v1.toInt
  }
  println(stringToInt("10"))



  // syntax for 2 parameters
  val function2:(Int,Int)=>Int = (x,y) => x+y
  println(function2(10,20))



  //Convert method to function using ETA-EXPANSION
  def addNumber(x:Int,y:Int)=x+y
  val convertToFunction= addNumber _
  

  //curried function
  val x:Int=>Int=>Int = x=>y=> x+y
  x(10)(20)

}



trait MyFunction[A,B] {
  def apply(x:A) :B
}
