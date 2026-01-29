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

object LongestSubString {


  def execute(str: String) = {
    var left = 0
    var maxLength = 0
    val lastSeen = collection.mutable.Map[Char, Int]() //here we store char and index
    for (right <- str.indices) {
      val char = str(right)
      if (lastSeen.contains(char) && lastSeen(char) >= left) {
        left = lastSeen(char) + 1
      }
      lastSeen(char) = right
      maxLength = math.max(maxLength, right - left + 1)
    }
    maxLength
  }

  def main(args: Array[String]): Unit = {
    println(execute("bbb"))
  }

}