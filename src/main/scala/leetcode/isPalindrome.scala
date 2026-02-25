package leetcode

/**
 * Created by Shyamrao on Feb 25, 2026.
 */

object isPalindrome extends App {


  def isPalindrome(str: String): Boolean = {
    var low  = 0
    var high = str.length - 1
    while (low < high) {
      while (low < high && !str(low).isLetterOrDigit) low += 1
      while (low < high && !str(high).isLetterOrDigit) high -= 1

      if (str(low).toLower != str(high).toLower) return false
      low += 1
      high -= 1
    }
    true
  }

  println(isPalindrome("A man a plan a canal Panama"))

}
