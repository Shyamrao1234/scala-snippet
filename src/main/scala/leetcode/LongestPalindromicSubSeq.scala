package leetcode

import scala.util.control.Breaks.break

object LongestPalindromicSubSeq extends App {


  def longestPalindrome(s: String): (Int, String) = {
    val revStr = s.reverse

    def f(len1: Int, len2: Int): (Int, String) = {
      if (len1 < 0 || len2 < 0) (0, "")
      else if (s(len1) == revStr(len2)) {
        val (count, seq) = f(len1 - 1, len2 - 1)
        (count + 1, seq + s(len1))
      } else {
        val left = f(len1 - 1, len2)
        val right = f(len1, len2 - 1)
        if (left._1 >= right._1) left else right
      }
    }

    f(s.length - 1, revStr.length - 1)
  }

  println(longestPalindrome("aacabdkacaa"))


}

object LPSV2 extends App {


  def longestPalindrome(str: String) = {
    var lpc = ""
    if (str.length <= 1) lpc = str

    for (i <- 0 to str.length - 1) {
      var low = i
      var high = i
      while (low >= 0 && high < str.length && str.charAt(low) == str.charAt(high)) {
        low -= 1
        high += 1
      }

      val palindrome = str.substring(low + 1, high)
      if (palindrome.length > lpc.length) lpc = palindrome


      low = i
      high = i + 1
      while (low >= 0 && high < str.length && str.charAt(low) == str.charAt(high)) {
        low -= 1
        high += 1
      }

      val palindrome2 = str.substring(low + 1, high)
      if (palindrome2.length > lpc.length) lpc = palindrome2
    }
    lpc
  }

  longestPalindrome("abaca")


}