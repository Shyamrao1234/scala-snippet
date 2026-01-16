package oopsbasic

import scala.concurrent.blocking

object ExceptionAndErrors extends App{


  new NullPointerException()


  //Can we catch error
  def factorial(n:BigInt):BigInt = {
    if(n==0) 1 else n * factorial(n-1)
  }

  try{
    10/0
    factorial(10000L)
  }catch {
    case stack:StackOverflowError => println("StackOverFlow")
    case e:Throwable=> println("Throwable") //output = null
    case _=>println(":Not found")
  }


  //Either
  def devide(a:Int,b:Int):Either[String,Int] ={
    if(b==0) Left("Error") else Right(1)
  }

  devide(1, 0) match {
    case Left(error) => println("error")
    case Right(value) => println(value)
  }


  val x:Either[Int,String] = if(true) Left(10) else Right("shyam")



  // my program should crash with OutOfMemory exception
  //  val array=new Array[Int](Int.MaxValue)


  println(Int.MinValue - 1)

}
