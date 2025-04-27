package leetcode

object LeetCode3 extends App {

  def lengthOfLongestSubstring(s: String): Int = {
    var maxLength = 0
    var start = 0
    val lastSeen = scala.collection.mutable.Map[Char, Int]()

    for ((char, end) <- s.zipWithIndex) {
      if (lastSeen.contains(char) && lastSeen(char) >= start) {
        start = lastSeen(char) + 1
      }
      lastSeen(char) = end
      maxLength = math.max(maxLength, end - start + 1)
    }

    maxLength
  }

  // Example usage
  val input = "abcabcbb"
  println(lengthOfLongestSubstring(input))

}
