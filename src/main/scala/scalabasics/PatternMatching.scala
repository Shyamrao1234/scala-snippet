package scalabasics

import scala.util.Random

object PatternMatching extends App {

  val random=new Random
  val x=random.nextInt(10)
  val description = x match {
    case 1=>"one"
    case 2=>"two"
    case 3 => "three"
    case _=>"wildCard"
  }

  println(x)
  println(description)


  case class Person(name:String,age:Int)

  val bob=Person("shyam",15)

  val personMatch=bob match {
    case Person(name,age) if(age>18) =>s"name : $name, age : $age"
    case _ => "WildCard"
  }

  println("Person : "+personMatch)

  case class Employee(name:String,age:Int)

  val employee=Employee("Rocky",15)
  val employeMatch=employee match {
    case emp:Employee if emp.age>18 => s"Employee Name: ${emp.name}, age : ${emp.age}"
    case _ => "Employee Match wildcard"
  }

  println("employee match"+employeMatch)


  val e= 10
  val isEven = if(e % 2 ==0) true else false // why ???
  val isEvenNormal = e % 2 == 0




}
