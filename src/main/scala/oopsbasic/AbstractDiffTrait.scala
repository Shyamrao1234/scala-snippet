package oopsbasic

object  AbstractDiffTrait

/***
 *  Difference between abstract class and trait
 *
 *  1: You can only extend one abstraction class but multiple trait
 *  2: you can provide constructor parameter to abstract class
 */

abstract class Animal {
  def sound:Unit
}
abstract  class Carnivore
case class Dog(name:String) extends Animal {
  override def sound: Unit = println("bark")
}

trait Bank
trait SubBranch
case class BangaloreBranch(name:String) extends Bank with SubBranch


object Main extends App{
  //Anonymous class

  val dog:Animal = new Animal {
    override def sound: Unit = println("Bark")
  }
}
