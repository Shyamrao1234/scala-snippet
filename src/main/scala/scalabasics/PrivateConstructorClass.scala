package scalabasics

class PrivateConstructorClass



/** Making constructor private will not allow anyone to create object outside the class or companion object
 *
 *Hide constructor details from users
 * */
class Temple private(name:String)

object Temple {
  def apply() = new Temple("AHYm")
}

class India{
  val data = Temple


}

object Demo extends App{

  println(Some(null).isDefined)
  println(Option(null).isDefined)
}