package oopsbasic

object OOBasics extends App{

  val person=new Person_1("John",26)
  println(person.age)

}

class Person_1(name:String,_age:Int) {

  def age: Int = _age
}