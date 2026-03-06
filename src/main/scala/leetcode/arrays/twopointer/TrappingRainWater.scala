package leetcode.arrays.twopointer

/**
 * Created by Shyamrao on Mar 05, 2026.
 */


/** *
 * First we will solve using brute force approach
 */

object TrappingRainWater extends App {

  def executeBruteForce(nums: Array[Int]) = {
    var totalWater = 0
    for (i <- 0 until nums.length) {

      var leftMax  = 0
      var rightMax = 0

      for (j <- 0 to i) {
        leftMax = Math.max(leftMax, nums(j))
      }

      for (k <- i until nums.length) {
        rightMax = Math.max(rightMax, nums(k))
      }

      val calculate = Math.min(leftMax, rightMax) - nums(i)
      totalWater += calculate
    }
    totalWater
  }


  def executeOptimisedWay(nums: Array[Int]) = {
    var left       = 0
    var right      = nums.length -1
    var leftMax    = 0
    var rightMax   = 0
    var totalWater = 0
    while (left < right) {
      if (nums(left) < nums(right)) {
        left += 1
        leftMax = Math.max(leftMax, nums(left))
        totalWater += leftMax - nums(left)
      } else {
        right -= 1
        rightMax = Math.max(rightMax, nums(right))
        totalWater += rightMax - nums(right)
      }
    }
    totalWater
  }

  println(executeOptimisedWay(Array(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1)))


}
