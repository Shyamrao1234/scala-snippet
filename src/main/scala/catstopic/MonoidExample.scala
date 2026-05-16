package catstopic

import java.nio.charset.StandardCharsets

object MonoidExample extends App {

  import cats.Semigroup
  import cats.instances.int._
  import cats.instances.string._
  import cats.syntax.semigroup._
  import cats.instances.byte._



  //  def semiCombineAll[T: Semigroup](list: List[T]): T = {
  //    list.foldLeft(0)(_ |+| _) // Note : since we dont know default value, to achieve this monoid is required
  //  }

  import cats.Monoid
  import cats.syntax.MonoidSyntax

  def monoidCombineAll[T: Monoid](list: List[T]): T = {
    list.foldLeft(Monoid.empty[T])(_ |+| _)
  }

  val intList = (1 to 1000).toList
  val stringList = List("S", "h", "y", "a", "m")
  val listOfBytes: List[Byte] = List(72, 101, 108, 108, 111)
  val listOfInt = List(72, 101, 108, 108, 111)

  val combinedValue = monoidCombineAll(listOfBytes)

  val bytesToUtf = new String(List(combinedValue).toArray, StandardCharsets.UTF_8)

  println(bytesToUtf)


  /**
   * Options behave like small collection
   * */
  val opt1: Option[Int] = Some(10)
  val opt2: Option[Int] = None
  val res = opt2 ++ opt1

  /** Will return iterable[Int] */
  println("Option combine with ++ --- " + res)


  /**
   * Options in Monoid
   *
   */

  import cats.instances.option._

  val monoidOption1 = Monoid[Option[Int]].combine(Some(10), Some(2))
  println("case 1 : combining to option : " + monoidOption1)

  val monoidOption2 = Monoid[Option[Int]].combine(Some(2), None)
  println("case 2 : Combine some and None : " + monoidOption2)

  /** *
   * Shopping card with monoid
   */
  case class ShoppingCard(items: List[String], total: Double)

  def combineAllShopping(list: List[ShoppingCard]): ShoppingCard = {
    val funtion = (s1: ShoppingCard, s2: ShoppingCard) => ShoppingCard(s1.items ++ s2.items, s1.total + s2.total)
    val emptyShoppingCard = ShoppingCard(List.empty[String], 0)
    implicit val shoppingCardMonoid = Monoid.instance[ShoppingCard](emptyShoppingCard, funtion)
    list.foldLeft(Monoid.empty[ShoppingCard])(_ |+| _)
  }

  val ListOfShoppingCards = List(
    ShoppingCard(List("SBI"), 2),
    ShoppingCard(List("HDFC"), 4)
  )
  println("Shopping card example for Empty list :" + combineAllShopping(List.empty[ShoppingCard]))
  println("Shopping card example for list values :" + combineAllShopping(ListOfShoppingCards))

}


object A extends App {

  import cats.instances.option._
  import cats.syntax.monoid._
  import cats.instances.int._
  import cats.instances.string._

  val res = Option(1) |+| Option(2)


  import cats.instances.map._

  val phonBooks = List(Map("alice" -> 123), Map("bob" -> 455))

  println("Using flatmap : " + phonBooks.flatten.toMap)
  println("Combining map :" + phonBooks.reduce((p1, p2) => p1 |+| p2))


  /** *
   * If map has same key then, monoid will add the vaues
   */

  val mapMonoid1 = Map("a" -> "a") |+| Map("a" -> "a", "b" -> "b")
  println("Same Map key" + mapMonoid1)


}