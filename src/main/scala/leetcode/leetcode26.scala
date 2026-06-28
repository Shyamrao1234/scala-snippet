package leetcode


/**
 *
 * LeetCode 26: Remove Duplicates from Sorted Array
 * Problem Statement
 *
 * You are given a sorted array nums.
 *
 * You must remove duplicates in-place such that:
 *
 * each unique element appears only once
 * relative order remains same
 * return the number of unique elements k
 *
 * The first k elements of the array should contain the unique values.
 *
 */


object leetcode26 extends App {


  def executeSimple(nums: Array[Int]) = {
    val newArray = new Array[Int](nums.length)
    var duplicateCount = 0
    var arrayCount = 0
    for (i <- nums.indices) {
      if (newArray.contains(nums(i))) {
        duplicateCount += 1
      } else {
        newArray(arrayCount) = nums(i)
        arrayCount += 1
      }
    }

    for(i<- arrayCount until  nums.length){
      newArray(i) = 0
    }
    newArray
  }

  println(executeSimple(Array(1, 1, 1, 1)).mkString("Array(", ", ", ")"))


}
