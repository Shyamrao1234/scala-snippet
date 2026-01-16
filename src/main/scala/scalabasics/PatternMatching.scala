package scalabasics

import scala.util.Random

object PatternMatching extends App {

  val random = new Random
  val x = random.nextInt(10)
  val description = x match {
    case 1 => "one"
    case 2 => "two"
    case 3 => "three"
    case _ => "wildCard"
  }

  println(x)
  println(description)


  case class Person(name: String, age: Int)

  val bob = Person("shyam", 15)

  val personMatch = bob match {
    case Person(name, age) if (age > 18) => s"name : $name, age : $age"
    case _ => "WildCard"
  }

  println("Person : " + personMatch)

  case class Employee(name: String, age: Int)

  val employee = Employee("Rocky", 15)
  val employeMatch = employee match {
    case emp: Employee if emp.age > 18 => s"Employee Name: ${emp.name}, age : ${emp.age}"
    case _ => "Employee Match wildcard"
  }

  println("employee match" + employeMatch)


  val e = 10
  val isEven = if (e % 2 == 0) true else false // why ???
  val isEvenNormal = e % 2 == 0


  //Match tuples
  val tuple = (1, 2)
  val matchTuple = tuple match {
    case (1, someThing) => println(" 1, " + someThing)
    case _ => println("Tuple wildcard")
  }

  val nestedTuple = (1, (1, 2))
  val nextedMatch = nestedTuple match {
    case (x, (y, z)) => println(s"$x , $y  $z")
    case _ => println("wildcard")
  }


  //List
  val list_V2 = List(1, 2, 3, 4)
  val match_list_V2 = list_V2 match {
    case a :: b :: tail => println(s"a : ${a}, b : ${b}, tail : ${tail}")
    case _ => println("wild card")
  }

  //infix
  val infixMatch = list_V2 match {
    case List(1, _*) => print("case-1")
    case List(1, 2, 3) :+ 4 => print("case-2")
    case 1 :: List(_) => print("case-3")
    case List(1, _, _, _) => print("case-4")
  }

  val number:List[Any] =List(1,2,3,4)
  val numberMatch = number match {
    case stringList:List[String] => println("string list")  //output will be this
    case numList:List[Int] => println("number list")
    case _ => println("wildCard number")
   }


  val fixNumberListMatch = number match {
    case List(_:String ,_*) =>println("list of string")
    case List(_: Int,_*)=>println("list of number")
  }

  fixNumberListMatch

}