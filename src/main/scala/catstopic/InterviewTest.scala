package catstopic


object InterviewTest extends App {

  /**
   * Multiple implicit conflict
   */

  import cats.Semigroup
  import cats.syntax.semigroup._
  import cats.instances.int._

  implicit val multiplySemiGroup: Semigroup[Int] = _ * _
  implicit val addSemiGroup: Semigroup[Int] = _ + _

  def reduceAll[T: Semigroup](list: List[T]): T = {
    list.reduce(_ |+| _)
  }

  println("🔥🔥🔥 IF YOU DO NOT SEE THIS, YOU ARE RUNNING GHOST CODE 🔥🔥🔥")
  println(reduceAll(List(1, 2, 3, 4, 44)))

}


case class UserStats(loginCount: Int, favoriteCategories: Set[String])


object UserStats {
  import cats.Semigroup
  import cats.syntax.semigroup._

  implicit val userStatsSemigroup = Semigroup.instance[UserStats] { (userStat1, userStat2) =>
    val addLoginCount = userStat1.loginCount + userStat2.loginCount
    val favCatUnion = userStat1.favoriteCategories.union(userStat2.favoriteCategories)
    UserStats(addLoginCount, favCatUnion)
  }


  def reduceThings[A: Semigroup](list: List[A]): A = {
    list.reduce { (u1, u2) => u1 |+| u2 }
  }

  val listOfUserStats = List(
    UserStats(1, Set("A", "B")),
    UserStats(2, Set("B", "C"))
  )

  def main(args: Array[String]): Unit = {
    println(reduceThings(listOfUserStats))
  }


}