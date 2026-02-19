package leetcode

object ReverseString extends App {


  def reverseCharArray(arr: Array[Char]) = {
    var left = 0
    var right = arr.length - 1

    while (left < right) {
      var temp = arr(left)
      arr(left) = arr(right)
      arr(right) = temp
      left += 1
      right -= 1
    }
    arr
  }

  reverseCharArray(Array('a', 'b', 'c', 'd')).foreach(println)

}

object LargestNumberInAnArray extends App {

  def largestNumber(nums: Array[Int]) = {
    var max = nums(0)
    for (i <- nums) {
      if (i > max) {
        max = i
      }
    }
    max
  }

  println(largestNumber(Array(1, 2, 3, 4)))


}

/**
 * -Binary search
 *
 * Array of list should be sorted.
 * divide array into 2 halves
 */


object BinarySearchDemo extends App {

  def binarySearch(nums: Array[Int], target: Int): Int = {
    var left = 0
    var right = nums.length - 1

    while (left <= right) {
      val mid = left + (right - left) / 2
      if (nums(mid) == target) return mid
      else if (nums(mid) < target) left = mid + 1
      else right = mid - 1
    }

    -1
  }



  println(binarySearch(Array(1, 2, 3), 2))


}