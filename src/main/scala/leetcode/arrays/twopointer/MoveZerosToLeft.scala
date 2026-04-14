package leetcode.arrays.twopointer

/**
 * Created by LENOVO on Apr 12, 2026.
 */

object MoveZerosToLeft extends App{

  private def execute(nums: Array[Int]) = {
    var left = 0
    for (right <- nums.indices) {
      if (nums(right) != 0) {
        nums(left)=nums(right)
        nums(right)=0
        left+=1
      }
    }
    nums
  }

  execute(Array(0,1,0,4,3)).foreach(println)

}
