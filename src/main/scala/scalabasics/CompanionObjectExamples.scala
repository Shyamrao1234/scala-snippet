package scalabasics

class CompanionObjectExamples {

}

class Circle(val radius: Double)

object Circle {

  def apply(r: Double) = new Circle(r)

  def unapply(c: Circle) = Some(c.radius)

}

object test extends App{

  val c=Circle(10.2)


  c match {
    case Circle(r) => println("radius : >>>"+r)
  }

}


/**
 * Static Methods breaks OOPs model why? In pure object oriented programming everything should be part of object
 * but static is part of class reference
 *
 *
 * static methods cannot be override
 * static method initialized in compile time
 * */


