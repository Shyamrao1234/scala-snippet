package leetcode.arrays.twopointer

/**
 * Created by Shyamrao on Mar 02, 2026.
 */

/** *
 *
 */

object ContainerWithMostWater extends App{

  def execute(nums: Array[Int]): Int = {
    var maxArea = 0
    var low     = 0
    var high    = nums.length - 1
    while (low < high) {
      val heigh = Math.min(nums(low), nums(high))
      val width = high - low
      val area  = heigh * width
      maxArea = Math.max(area, maxArea)
      if (nums(low) < nums(high)) low += 1
      else high -= 1
    }
    maxArea
  }

  println(execute(Array(1,1)))

}
