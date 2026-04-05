package leetcode.collectionprograms

/**
 * Created by Shyamrao on Mar 17, 2026.
 */

object AllCollectionsPrograms {

}

object FirstNonRepeatingChar extends App {


  def firstNonRepeatingChar(s: String) = {
    val freq = scala.collection.mutable.LinkedHashMap[Char, Int]()

    s.foreach(c => freq(c) = freq.getOrElse(c, 0) + 1)

    freq.find(_._2 == 1).map(_._1)
  }


  println(firstNonRepeatingChar("swiss"))

}

object FindDuplicateElements extends App {

  def findDuplicateElements(list: Seq[Int]) = {
    list.groupBy(identity).collect { case (k, v) if v.length > 1 =>
      k
    }.toList
  }

  println(findDuplicateElements(List(1, 2, 2, 3, 3, 4, 4, 5)))
}

object WordFrequencyCounter extends App {

  def wordFrequencyCounter(sentence: String) = {
    sentence
      .split(" ")
      .groupBy(identity)
      .map { case (k, v) => (k -> v.length) }
  }

  println(wordFrequencyCounter("scala is powerful and scala is functional"))
}


object longestConsecutive extends App {


  List(1, 2, 3, 4).foldLeft(0) { (acc, num) =>
    println(acc)
    num
  }


}



