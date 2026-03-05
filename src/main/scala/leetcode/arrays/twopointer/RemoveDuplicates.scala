package leetcode.arrays.twopointer

/**
 * Created by Shyamrao on Feb 25, 2026.
 */

object RemoveDuplicates extends App {


  def removeDuplicates(array: Array[Int]): Int = {
    if (array.isEmpty) return 0
    var slow = 0

    for (fast <- 1 until array.length) {
      if (array(slow) != array(fast)) {
        slow += 1
        array(slow) = array(fast)
      }
    }

    array.foreach(println)
    slow + 1
  }


  println(removeDuplicates(Array(1, 2, 2, 3)))

}
