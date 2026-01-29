package leetcode

object LeetCode1 extends App {


  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val numMap = scala.collection.mutable.HashMap[Int, Int]()

    for (i <- nums.indices) {
      val complement = target - nums(i)
      if (numMap.contains(complement)) {
        return Array(numMap(complement), i)
      }
      numMap(nums(i)) = i
    }

    Array() // No solution found
  }


  println(twoSum(Array(2, 7, 11, 15), 9).mkString("Array(", ", ", ")"))
}


object TwoSumRecap extends App {

//  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
//
//  }

  val arr = Array(1, 2, 3, 4, 5, 6, 7)
  println(arr.length)

}