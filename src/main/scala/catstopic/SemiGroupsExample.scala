package catstopic

import cats.Semigroup

object SemiGroupsExample {

  import cats.instances.int._
  import cats.instances.string._
  import cats.instances.list._
  import cats.instances.option._

  val intSemigroup = Semigroup[Int]
  val combineIntSemigroup = intSemigroup.combine(23, 43)

  val stringSemigroup = Semigroup[String]
  val combineStringSemigroup = stringSemigroup.combine("I love", " cats")

  val intOptionSemigroup = Semigroup[Option[Int]]
  val combineIntOption = intOptionSemigroup.combine(Some(90), Some(10))

  def reduceInts(list: List[Int]) = list.reduce(intSemigroup.combine)

  def reduceString(list: List[String]) = list.reduce(stringSemigroup.combine)

  //general api
  def reduceAll[T](list: List[T])(implicit semiGroup: Semigroup[T]): T = list.reduce(semiGroup.combine)


  //Implement semigroup for case class
  case class Expense(id: Long, amount: Double)

  implicit val expenseSemiGroup = Semigroup.instance[Expense] { (e1, e2) =>
    Expense(Math.max(e1.id, e2.id), e1.amount + e2.amount)
  }

  //Extension method for semigroup : |+|

  import cats.syntax.semigroup._

  val combineInt = 1 |+| 2
  val combineString = "I love " |+| "scala"
  val combineCaseClass = Expense(1, 34) |+| Expense(2, 45)


  //TODo : Implement a reduceAll2 with this fancy syntax
  def reduceAll2[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(_ |+| _)

  def reduceAll3[T: Semigroup](list: List[T]): T = list.reduce(_ |+| _)


  def main(args: Array[String]): Unit = {
    println(combineIntSemigroup)
    println(combineStringSemigroup)
    println("***Option Sume***" + combineIntOption)
    println(reduceAll(List("I love ", "valley ball")))


    import cats.instances.option._
    val listOfOption = (1 to 10).map(each => Option(each)).toList
    println(reduceAll(listOfOption))


    //call reduce all on case class
    val listOfExpenses = List(Expense(1, 20), Expense(2, 33), Expense(3, 44))
    println("***case class**" + reduceAll(listOfExpenses))


   

  }

}


object SemiGroupPractice extends App {


  import cats.Semigroup._
  import cats.instances.int._

  val intSemiGroup = Semigroup[Int]
  println(in)

}