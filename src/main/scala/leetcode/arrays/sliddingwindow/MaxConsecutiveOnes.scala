package leetcode.arrays.sliddingwindow

/**
 * Created by Shyamrao on Mar 04, 2026.
 */


/** *
 * find max consecutive ones in an array using brute force approach
 */

import scala.util.control.Breaks._

object MaxConsecutiveOnes extends App{

  def execute(nums: Array[Int]) = {
    var maxSum = 0
    for (i <- 0 until nums.length) {
      var count = 0
      breakable {
        for (j <- i until nums.length) {
           if(nums(j)==1){
             count+=1
             maxSum=Math.max(maxSum,count)
           }else break()
        }
      }
    }
    maxSum
  }


  println(execute(Array(1,1,0,1,1,1,1)))


}
