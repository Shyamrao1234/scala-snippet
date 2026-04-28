package leetcode

/**
 * Created by Shyamrao on Apr 17, 2026.
 */


/** *
 *
 * Input: [[1,3], [2,6], [8,10], [15,18]]
 * Output: [[1,6], [8,10], [15,18]]
 *
 */


object MergeIntervals extends App {


  def mergeOverlappingIntervals(intervals: List[Array[Int]]) = {
    val sortedIntervals = intervals.sortBy(innerList => innerList(0))

    sortedIntervals.foldLeft(List.empty[Array[Int]]) { case (acc, current) =>
      acc match {
        case Nil          => List(current)
        case ::(head, tl)if current(0) <= head(1) =>
        Array(head(0),math.max(current(1),head(1))) :: tl
        case _            => current :: acc
      }
    }
  }


  mergeOverlappingIntervals(List(Array(1,3),Array(2,6),Array(8,10),Array(15,18))).reverse.map(_.foreach(print))

}
