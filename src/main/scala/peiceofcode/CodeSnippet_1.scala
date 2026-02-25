package peiceofcode

/**
 * Created by Shyamrao on Feb 24, 2026.
 */

object CodeSnippet_1 extends App {

  def test(obj: Object) = {
    println("obj")
  }

  def test(str: String) = {
    println("string method")
  }


  test(null)

}


trait MyFunction {

  def test: Unit
}

object CodeSnippet_2 extends App {
  var x             = 10
  val f  = new MyFunction {
    override def test: Unit = println(x)
  }
  x = 20
  f.test

}