package leetcode

object MergeIntervals extends App {


  /**
   *
   * Array(Interval(1,4),Interval(2,6),Interval(7,9))
   *
   */

   case class Interval(start: Int, end: Int)
   private def execute(intervals: Array[Interval]) = {
    val sortedIntervals = intervals.sortBy(_.start)

    var merged = List(sortedIntervals.head)
    for (i <- 1 until intervals.length) {
      val lastMerged = merged.head
      val current = sortedIntervals(i)
      if (lastMerged.end >= current.start) {
        val updatedInterval = Interval(lastMerged.start, Math.max(lastMerged.end, current.end))
        merged = updatedInterval :: merged.tail
      } else {
        merged = current :: merged
      }
    }
    merged.reverse
  }


  val listOfInterval=Array(Interval(1,4),Interval(2,6),Interval(7,8))
  println("*****"+execute(listOfInterval))


}
