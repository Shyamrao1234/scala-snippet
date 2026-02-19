package leetcode

object MoveZeroToEnd extends App {

  def execute(nums: Array[Int]) = {
    val arrayLength= nums.length-1
    var nonZeroIndex = 0
    var temp = 0
    for (i <- 0 to arrayLength) {
      if (nums(i) != 0) {
        temp = nums(nonZeroIndex)
        nums(nonZeroIndex) = nums(i)
        nums(i) = temp
        nonZeroIndex += 1
      }
    }
    nums
  }

  execute(Array(0,1,2,0,3)).foreach(print)



}
