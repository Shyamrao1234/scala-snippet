package leetcode

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object MaximumSubarray extends App {

//
//  def maxSubArray(nums: Array[Int]) = {
//    var currentSum = nums(0)
//    var maxSum = nums(0)
//
//    for (i <- 1 until nums.length) {
//      currentSum = Math.max(nums(i), currentSum + nums(i))
//      maxSum = Math.max(maxSum, currentSum)
//    }
//
//    maxSum
//  }
//
//  println(maxSubArray(Array(4, -1, 2, 1)))

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  val f1 = Future {
    Thread.sleep(1000)
    1
  }

  val f2 = Future {
    Thread.sleep(1000)
    1/0
    2
  }

  println(Await.result(f1, 1.seconds))
  println(Await.ready(f2, 1.seconds))


}
