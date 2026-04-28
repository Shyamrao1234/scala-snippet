package scalabasics

/**
 * Created by LENOVO on Apr 18, 2026.
 */

object GenericsExample extends App {


  /** *
   * Upper bound restricts the type, other than subtype of any class
   */

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  def checkUpperBound[A <: Animal](animal: A) = {
    println(animal)
  }

  println(checkUpperBound(new Dog()))
  println(checkUpperBound(new Cat()))


}
