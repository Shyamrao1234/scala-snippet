package scalabasics

class InheritanceMixinsVisibilityModifiers


class Animal {

  def eat(): Unit = println("Animal is eating")

  protected def sleep(): Unit = println("Animal is sleeping")

  private def secret() = println("animals secret")

  def showSecret() = secret()

  val add = (a: Int, b: Int) => a + b
}


trait Trail {
  def wagTail(): Unit = println("Wagging tail 🐕")
}

trait Bark {
  def bark() = println("Bark! Bark!")
}

class Dog extends Animal with Trail with Bark {

  override def eat(): Unit = {
    println("Dog is eacting")
  }

  override protected def sleep(): Unit = {
    println("overrided method")
  }

  def goToSleep() = {
    sleep()
  }
}

object Main extends App {

  val dog = new Dog

  dog.eat();
  dog.goToSleep()
  dog.bark()
  dog.wagTail()


}





