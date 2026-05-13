package catstopic

/**
 * Created by LENOVO on Apr 27, 2026.
 */

object TypeClasses extends App {

  case class Person(name: String, age: Int)

  //Part 1 : type class definition
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  //Part 2 :create implicit type class INSTANCES
  implicit object StringSerializer extends JSONSerializer[String] {
    override def toJson(value: String): String = "\"" + value + "\""
  }

  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJson(value: Int): String = value.toString
  }

  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
         |{"name":${value.name},"age":${value.age}}
         |""".stripMargin.trim
  }


  // offer some api
  def convertListToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String = {
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")
  }

  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer[T]) {
      def toJson = serializer.toJson(value)
    }
  }

  println(convertListToJson(List(Person("Shyam", 20), Person("Alice", 32))))

  import JSONSyntax._

  println(s"***** ${Person("name", 21).toJson} *******")

}

object TypeClassCatsExample extends App {

  trait Eq[T] {
    def eqv(a: T, b: T): Boolean
  }

  implicit val intEq: Eq[Int] = new Eq[Int] {
    override def eqv(a: Int, b: Int): Boolean = a == b
  }


  def isEqual[T](a: T, b: T)(implicit eq: Eq[T]) = {
   eq.eqv(a,b)
  }

  println(isEqual(2,3))


}
