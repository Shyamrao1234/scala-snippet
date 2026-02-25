package leetcode.arrays

/**
 * Created by Shyamrao on Feb 21, 2026.
 */

object ThreeNumberSum extends App {


  /** *
   * We will use three pointer
   *
   * Array[0,-1,]
   */
  def threeSum(array: Array[Int]) = {
    val sortedArray = array.sorted
    val list        = scala.collection.mutable.ListBuffer[List[Int]]()

    for (i <- 0 to sortedArray.length - 2) {
      var left  = i + 1
      var right = sortedArray.length - 1
      if (i == 0 || sortedArray(i) != sortedArray(i - 1)) {
        while (left < right) {
          val sum = sortedArray(left) + sortedArray(right) + sortedArray(i)
          if (sum == 0) {
            list += List(sortedArray(left), sortedArray(right), sortedArray(i))
            while (left < right && sortedArray(left) == sortedArray(left + 1)) left += 1
            while (left < right && sortedArray(right) == sortedArray(right - 1)) right -= 1
            left += 1
            right -= 1
          } else if (sum > 0) {
            right -= 1
          } else left += 1
        }
      }
    }
    list.foreach(println)
  }

  threeSum(Array(-1, 0, 1, 2, -1, -4))
  


}