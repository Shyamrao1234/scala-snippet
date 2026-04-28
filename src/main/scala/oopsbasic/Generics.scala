package oopsbasic

object Generics extends App {

  class Animal

  class Cat extends Animal

  class Dog extends Animal

  class CovariantList[+A]

  val covariantAnimalList: CovariantList[Animal] = new CovariantList[Cat]

  class InvariantList[A]

  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  class Trainer[-T]

  val trainerList: Trainer[Cat] = new Trainer[Animal]


}

/** *
 *
 * stack overflow question.
 *
 *
 */

class Animal {
  def name: String = "Animal"
}

class Dog extends Animal {
  def nam: String = "Dog"
}


class SpecificAnimalContainer[A <: Animal](a: A) {
  def specificAnimal: A = a
}

object TestAnimal extends App {

  val spec1 = new SpecificAnimalContainer[Dog](new Dog())
  val spec2 = new SpecificAnimalContainer[Animal](new Animal) // my requirement is I dont want this be the implemented
}


/**
 * Why can't we use mutable variable in covariance
 */

class Box[+T]

object Covariance {

  class Animal

  class Dog extends Animal

  val boxDogs             = new Box[Dog]
  val animal: Box[Animal] = boxDogs

}


object Contravariance {

  class Box[-A]

  class Animal

  class Dog extends Animal

  val boxAnimal        = new Box[Animal]
  val dogBox: Box[Dog] = boxAnimal

}

object Invariance {
  class Box[T]
  class Animal
  class Dog extends Animal

  val boxDog                 = new Box[Dog]
}