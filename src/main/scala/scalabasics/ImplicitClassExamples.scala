//package scalabasics
//
//import org.json4s.JsonAST.JValue
//
//
//case class Person(name:String,age:Int)
//
//object ImplicitClassExamples extends App{
//
//  implicit class MyRichInt(number:Int) {
//   def isEven = number%2 == 0
//  }
//
//  println(2.isEven) // Internally it works like this new MyRichInt(2).isEven
//
//  //conversions
//  implicit  def string2person(name:String):Person = {
//    Person(name,24)
//  }
//
//  val shyam:Person="shyam"
//
//  println(shyam)
//
//  implicit class CustomJson(jValue: JValue) {
//
//
//
//    def asJson: String = {
//      ""
//    }
//  }
//
//
//
//}
