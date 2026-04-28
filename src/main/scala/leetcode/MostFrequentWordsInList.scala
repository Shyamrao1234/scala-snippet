package leetcode

/**
 * Created by LENOVO on Apr 23, 2026.
 */

object MostFrequentWordsInList extends App {


  private def execute(list: List[String]) = {
    val res = list.groupBy(identity).map(each => each._1 -> each._2.size)
    res.maxBy(_._2)
  }


  print(execute(List("apple", "banana", "apple", "orange", "banana", "apple")))


  def firstNonRepeating(str: String) = {
    val res = str.groupBy(identity).map(each => each._1 -> each._2.size)
    str.find(res(_) == 1)
  }

  print(firstNonRepeating("swiss"))


  val map = scala.collection.mutable.Map[String, Int]()

  val m1 = Map("a" -> 1, "b" -> 2)
  val m2 = Map("b" -> 3, "c" -> 4)

  val res = m2.foldLeft(m1) { case (acc, (key, value)) => {
    acc + (key -> (acc.getOrElse(key, 0) + value))
  }}

  print(res)


}
