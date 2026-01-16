package oopsbasic

object Generics extends App{

  class Animal
  class Cat extends Animal
  class Dog extends Animal

  class CovariantList[+A]
  val covariantAnimalList:CovariantList[Animal] = new CovariantList[Cat]

  class InvariantList[A]
  val invariantAnimalList:InvariantList[Animal] =  new InvariantList[Animal]

  class Trainer[-T]
  val trainerList:Trainer[Cat] = new Trainer[Animal]


}
