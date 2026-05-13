package catstopic

/**
 * Created by LENOVO on Apr 27, 2026.
 */

object CatsIntro extends App {

  val comparison = 2 == "shyam"

  //we have to check the typeSafety

  import cats.syntax.eq._
  import cats.instances.int._

  val catsComparison = 2 === 2
  println(catsComparison)

  //comparison for list
  val normalList = List(2) == List("shyam")

  import cats.instances.list._
  val catsComparisonList = List(1) === List(2)




}
