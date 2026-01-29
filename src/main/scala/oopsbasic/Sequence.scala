package oopsbasic

import scala.collection.immutable.ListMap
import scala.collection.mutable
import scala.util.Random

object Sequence extends App {


  val seq = Seq(1, 2, 3, 4, 5)
  println(seq)
  println(seq.reverse)
  println(seq.isEmpty)
  println(seq.map(n => n -> "Hello").toMap)
  println(ListMap(seq.zipWithIndex: _*))
  seq.sliding(2, 3).foreach(println)


  case class Employee(name: String, age: Int)

  val employeeList = List(
    Employee("john", 22),
    Employee("akash", 25),
    Employee("Vikas", 26),
    Employee("jai", 47)
  )

  employeeList.sliding(2).foreach(println) //(john,akash),(akash,vikas),(vikas,jai)

  //Arrays
  val arrays = Array.ofDim[String](3)
  arrays.foreach(println)

  val numbersArray = Array(1, 2, 3, 4)
  numbersArray.update(2, 10)
  println(numbersArray.lift(10))
  numbersArray.foreach(println)


  // Difference between list and vector
  val maxRun = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]) = {
    val r = new Random
    val times = for {
      it <- 1 to maxRun
    } yield {
      val startTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - startTime
    }
    times.sum * 1.0 / maxRun
  }

  val numberList = getWriteTime((1 to maxCapacity).toList)
  val numberVector = getWriteTime((1 to maxCapacity).toVector)

  println(numberList)
  println(numberVector)

}
