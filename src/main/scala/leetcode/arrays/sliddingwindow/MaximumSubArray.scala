package leetcode.arrays.sliddingwindow

/**
 * Created by Shyamrao on Mar 02, 2026.
 */

object MaximumSubArray extends App {


  /** *
   * first will solve with brute force
   * Given an array Array(-2,1,-3,4,-1,2,1,-5,4)
   */


  private def execute(nums: Array[Int], k: Int) = {
    val arrayLength = nums.length
    var maxSum      = 0
    for (i <- 0 to arrayLength - k) {
      var sum = 0
      for (j <- i until k + i) {
        sum += nums(j)
      }
      maxSum = Math.max(maxSum, sum)
    }

    maxSum
  }


  /***
   * using sliding window
   */
  def executeInternal_2(nums: Array[Int], k: Int) = {
    var windowSum = 0
    var maxSum    = 0
    for (i <- 0 until k) {
      windowSum += nums(i)
    }
    maxSum = windowSum
    for (j <- k until nums.length) {
      windowSum += nums(j)
      maxSum = Math.max(windowSum, maxSum)
    }
  }


  println(executeInternal_2(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4), 4))

}
