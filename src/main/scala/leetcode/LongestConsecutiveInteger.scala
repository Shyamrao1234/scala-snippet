package leetcode

/**
 * Created by Shyamrao on Apr 30, 2026.
 */



/** *
 * Find longest length of consecutive integer in an array
 *
 * Condition : should execute O(n) time complexity
 * hints : use hashSet, find initial number
 */

object LongestConsecutiveInteger extends App {


  private def execute(nums: Array[Int]) = {
    val numsSet       = nums.toSeq
    var longestStreak = 0
    for (i <- 0 until nums.length) {
      var currentNum    = nums(i)
      var currentStreak = 1
      if (!numsSet.contains(currentNum - 1)) {
        while (numsSet.contains(currentNum + 1)) {
          currentStreak += 1
          currentNum += 1
        }
        longestStreak = Math.max(longestStreak, currentStreak)
      }
    }
    longestStreak
  }

  println(execute(Array(100,2,200,1,3,4)))


}
