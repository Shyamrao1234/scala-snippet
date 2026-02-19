package leetcode

object TwoSum extends App {


  /**
   * Given an integer array nums and a target, return indices of two numbers that add up to the target.
   */
  def findIndexOfTwoSum(nums: Array[Int], target: Int) = {
    val map = scala.collection.mutable.Map[Int, Int]()
    var index1, index2 = 0

    for (index <- nums.indices) {
      val needed = target - nums(index)
      if (map.contains(needed)) {
        index1 = map(needed)
        index2 = index
      }
      map(nums(index)) = index
    }
    (index1,index2)
  }

  println(findIndexOfTwoSum(Array(10,3,4),5))


}
