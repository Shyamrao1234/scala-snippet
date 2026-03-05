package leetcode.arrays.twopointer

/**
 * Created by Shyamrao on Feb 21, 2026.
 */

object SortColors extends App {


  def sortArray(array: Array[Int]) = {
    var low  = 0
    var mid  = 0
    var high = array.length - 1

    while (mid <= high) {
      if (array(mid) == 0) {
        array(mid) = array(low)
        array(low) = 0
        low += 1
        mid += 1
      } else if (array(mid) == 1) {
        mid += 1
      } else {
        array(mid) = array(high)
        array(high) = 2
        high -= 1
      }
    }
    array.foreach(print)
  }



  sortArray(Array(1,1,0,2,0,1,2,0,2))
}
