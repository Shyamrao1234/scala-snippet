package leetcode

object LeetCode4 extends App {


  val nums1 = Array[Int](1, 2, 3)
  val nums2 = Array[Int](5, 6,8)

  val res = (nums1 ++ nums2).sorted
  val mid = (res.length) / 2

  val result=if (res.length % 2 == 0) {
    (res(mid - 1) + res(mid)) / 2.0
  } else {
    res(mid).toDouble
  }

  println(result)

}
