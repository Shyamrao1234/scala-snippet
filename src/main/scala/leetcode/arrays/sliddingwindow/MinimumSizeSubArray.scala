package leetcode.arrays.sliddingwindow

import scala.util.control.Breaks._

/**
 * Created by Shyamrao on Mar 14, 2026.
 */

object MinimumSizeSubArray extends App {


  def minSubArrayLen(target: Int, nums: Array[Int]) = {
    var minLength = Int.MaxValue
    for (i <- nums.indices) {
      var sum = 0
      breakable {
        for (j <- i until nums.length) {
          sum += nums(j)
          if (sum >= target) {
            val count = j - i + 1
            minLength = Math.min(minLength, count)
            break()
          }
        }
      }
    }
    if (minLength == Int.MaxValue) 0 else minLength
  }


  def optimisedApproach(target: Int, nums: Array[Int]) = {
    var left   = 0
    var sum    = 0
    var answer = Int.MaxValue

    for (i <- nums.indices) {
      sum += nums(i)
      while (sum >= target) {
        answer = Math.min(i - left + 1, answer)
        sum -= nums(left)
        left +=1
      }
    }
    if (answer == Int.MaxValue) 0 else answer
  }


  println(optimisedApproach(15, Array(1, 2, 3, 4, 5)))


}
