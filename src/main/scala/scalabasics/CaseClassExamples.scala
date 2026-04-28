package scalabasics

/**
 * Created by LENOVO on Apr 23, 2026.
 */

/***
 *   -> what is case class?
 *   ans-> case class is immutable data modeling with auto boilerplate generation, designed for
 *   immutability, pattern patching and functional programing
 *
 */


object CaseClassExamples extends App {

  case class Address(city:String)
  case class User(address:Address)

  val u1=User(Address("Delhi"))
  val u2=u1.copy()

  println(u1 eq u2)  //false
  println(u1.address eq u2.address) //true

  println(u1.productArity) // gives number of constructor parameter
  println(u1.productElement(0)) // gives values of the parameter


  case class Config() {
    lazy val heavy = {
      Thread.sleep(1000)
      "done"
    }
  }

  val c = Config()

  (1 to 10).par.foreach(_ => println(c.heavy))

}
