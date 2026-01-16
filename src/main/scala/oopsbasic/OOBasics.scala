package oopsbasic

object OOBasics extends App{

  val person=new Person("John",26)
  println(person.age)

}

class Person(name:String,_age:Int) {

  def age: Int = _age
}