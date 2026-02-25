package scalabasics

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import jdk.nashorn.internal.ir.IdentNode

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ScalaInterviewPractice extends App {

  @tailrec
  def sumRec(n: Int, acc: Int): Int = {
    if (n == 0) acc
    else sumRec(n - 1, acc + n)
  }

  //  println("tail rec:  "+sumRec(1000000,0))

  def sumLoop(n: Int): Int = {
    var acc = 0
    var i   = n
    while (i != 0) {
      acc += i
      i -= 1
    }
    acc
  }

  //non-tail recursive
  @tailrec
  def nonTailRecSum(n: Int): Int = {
    if (n == 0) n
    else nonTailRecSum(n - 1)
  }

  //  println("loop "+nonTailRecSum(1000000))


  case class Person(name: String, age: Int)

  val p1 = Person("shyam", 10)
  val p2 = p1.copy(name = "rocky") //

  println(p1)
  println(p2)

}


class Employee(name: String, age: Int)

object Run extends App {

  var count = 0
  (1 to 100).par.foreach { _ =>
    count += 1
  }

  println(count)


}

object CounterRaceConditionSolve extends App {

  trait CountCommand

  case object Increment extends CountCommand

  case object Get extends CountCommand

  object Counter {
    def apply() = active()

    def active(counter: Int = 0): Behavior[CountCommand] = Behaviors.receive { (context, message) =>
      message match {
        case Get       => println("count: ->" + counter)
          Behaviors.same
        case Increment => active(counter + 1)
        case _         => Behaviors.same
      }

    }
  }

  val actorSystem = ActorSystem(Counter(), "Counter")

  (1 to 100000).par.foreach { _ =>
    actorSystem ! Increment
  }

  Thread.sleep(1000)
  actorSystem ! Get


  val x: Nothing = throw new Exception("")
}


object Play extends App {


  def aRepeatedString(str: String, number: Int): String = {
    if (number == 1) str else str + aRepeatedString(str, number - 1)
  }

  //normal recursive call
  def factorial(n: Int): Int = {
    if (n <= 0) 1 else n * factorial(n - 1)
  }

  def tailFactorial(n: Int, acc: Int = 1): Int = {
    if (n <= 0) acc else tailFactorial(n - 1, acc * n)
  }


  def fibonacci(n: Int): Int = {
    if (n <= 2) 1 else fibonacci(n - 1) + fibonacci(n - 2)
  }

  println("RepeatedString" + aRepeatedString("hello", 3))
  println("factorial....." + factorial(3))
  println("tailFactorial....." + tailFactorial(3))
  println("fibonacci...." + fibonacci(4))

  val list: List[Int] = List(1, 2, 3, 4, 5)
  list.foreach(x => println(x.asInstanceOf[AnyRef].getClass))

  val h = 10
  println("normal Int...." + h.asInstanceOf[AnyRef].getClass)

  //functions sugar
  val function1: Int => Int = x => x + 1
  println("function1...." + function1(10))

  val function2: Int => Int => Int = x => y => x + y
  val partialFunction              = function2(10)


  def isPrime(n: Int): Boolean = {

    def isUntilPrime(t: Int): Boolean = {
      if (t <= 1) true
      else n % t != 0 && isUntilPrime(t - 1)
    }

    isUntilPrime(n / 2)
  }

  println("isPrime...." + isPrime(4))
  println("partialFunction..." + partialFunction(20))
  println("function2...." + function2(10)(21))

  println("divide..." + (10 / 3))
  println("modbus..." + 10 % 3)

  println("square root " + Math.sqrt(10))
  println("Isprime..2....." + (if (2 % 2 != 0) true else false))


  //type inference
  val x1 = 10
  val y1 = x1 + "string"
  println("y1..." + y1)


  val listBuffer = scala.collection.mutable.ListBuffer[Int]()

  listBuffer += 4
  println(listBuffer)

  val list22 = List(1, 2, 3)
  println(list22 :+ 10)

}