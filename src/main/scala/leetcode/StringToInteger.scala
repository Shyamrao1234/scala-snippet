package leetcode

import scala.util.control.Breaks.{break, breakable}


/**
 * leetCode 8
 */
object StringToInteger extends App {

  val str = "0"

  def myAtoi(s: String): Int = {
    var str = s.trim
    var result: Long = 0
    var digit = 0
    var i = 0
    var sign = 1
    if (str(i) == '+' || str(i) == '-') {
      if (str(i) == '-') sign = -1
      i += 1
    }

    while (i < str.length && str(i).isDigit) {
      digit = str(i) - '0'
      result = result * 10 + digit
      if (result > Int.MaxValue) return if (sign == 1) Int.MaxValue else Int.MinValue
      i += 1
    }
    sign * result.toInt
  }

  println(myAtoi("-91283472332"))
}