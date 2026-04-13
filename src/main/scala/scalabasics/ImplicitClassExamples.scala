package scalabasics

import com.fasterxml.jackson.databind.JsonSerializer


case class Person(name: String, age: Int)

object ImplicitClassExamples extends App {

  implicit class MyRichInt(number: Int) {
    def isEven = number % 2 == 0
  }

  println(2.isEven) // Internally it works like this new MyRichInt(2).isEven

  //conversions
  implicit def string2person(name: String): Person = {
    Person(name, 24)
  }

  val shyam: Person = "shyam"

  println(shyam)

}


/** **
 * what are the used of implicits in scala
 *
 * 1 -> implicit parameter
 * 2 -> implicit conversions (Dangerous  used for method)
 * 3 -> implocit classed (type extension)
 *
 * Type class pattern
 *
 */

trait Show[T] {
  def show(value: T): String
}

object Test extends App {

  implicit val intShow: Show[Int] = (value: Int) => s"Int: $value"

  implicit val stringShow: Show[String] = (value: String) => s"String: $value"


  def printValue[T](value: T)(implicit show: Show[T]) = println(show.show(value))


  printValue("Scala")
  printValue(20)
}


case class Truck(b: Int)

object Truck {
  implicit val defaultTruck: Truck = Truck(10)
}

object TestTruck {


  def execute(implicit truck: Truck) = truck

  execute

}


object Serializer extends App {


  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  implicit def oneArgClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String = {
      s"""
        |${value.productElement(0)}
        |""".stripMargin
    }
  }


  case class Cat(name:String)


  println(oneArgClassSerializer[Cat].toJson(Cat("jonny")))




}
