package leetcode

//object LeedCode4V1 extends App{
//
//  def findMedianSortedArrays(nums1: Array[Int], nums2: Array[Int]): Double = {
//    // Ensure nums1 is the smaller array
//    val res  = if (nums1.length > nums2.length) (nums2, nums1) else (nums1, nums2)
//    val m = res._1.length
//    val n = res._2.length
//
//    var low = 0
//    var high = m
//
//    while (low <= high) {
//      val i = (low + high) / 2
//      val j = (m + n + 1) / 2 - i
//
//      val Aleft = if (i == 0) Int.MinValue else res._1(i - 1)
//      val Aright = if (i == m) Int.MaxValue else res._1(i)
//
//      val Bleft = if (j == 0) Int.MinValue else res._2(j - 1)
//      val Bright = if (j == n) Int.MaxValue else res._2(j)
//
//      if (Aleft <= Bright && Bleft <= Aright) {
//        // Found correct partition
//        if ((m + n) % 2 == 0) {
//          return (Math.max(Aleft, Bleft) + Math.min(Aright, Bright)) / 2.0
//        } else {
//          return Math.max(Aleft, Bleft).toDouble
//        }
//      } else if (Aleft > Bright) {
//        high = i - 1
//      } else {
//        low = i + 1
//      }
//    }
//
//    throw new IllegalArgumentException("Input arrays are not sorted properly.")
//  }
//
//  println(findMedianSortedArrays(Array(1,2,3,4),Array(5,6,7,8,9)))
//
//}


sealed trait TrafficLights {
  def a

  def b() = {
    "b"
  }
}


case object Red extends TrafficLights {
  override def a: Unit = ???

  override def b = ""
}
//case object Blue extends TrafficLights
//case object Green extends TrafficLights

object Execute extends App{

//  def fn(light:TrafficLights) = {
//    light match {
//      case Red => ""
//      case Blue => ""
//    }
//  }

}