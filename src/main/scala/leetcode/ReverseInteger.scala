package leetcode

import scala.runtime.Nothing$

object ReverseInteger extends App {


  def reverse(x: Int): Int = {

    var rev: Long = 0
    var num = x
    while (num != 0) {
      var digit = num % 10
      rev = rev * 10 + digit
      num = num / 10
    }
    if (rev < Int.MinValue || rev > Int.MaxValue) 0 else rev.toInt
  }


  println(reverse(1534236469))

  println(Int.MaxValue)

}

object ReverseInteger_V1 extends App {


  def reverseInteger(number: Int) = {
    var n = number
    var rev = 0
    while (n > 0) {
      val lastDidit = n % 10
      rev = (rev * 10) + lastDidit
      n = n/10
    }
    rev
  }

  def reverIntWithRecursion(num: Int, rev: Int=0):Int = {
    if (num <= 0) {
      rev
    } else {
      reverIntWithRecursion(num / 10, (rev * 10) + num % 10)
    }
  }
  println(reverIntWithRecursion(123))

  println(reverseInteger(1234))

}




















