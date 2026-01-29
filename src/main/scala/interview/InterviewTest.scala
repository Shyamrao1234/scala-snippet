package interview

object InterviewTest extends App {


  val x: Int => Int = x => x + 1

  def outerMethod(f: Int => Int, x: Int): Int = {
    f(x)
  }

  println(outerMethod(x, 10))

  case class Employee(name: String, age: Int)

  val list = List(Employee("A", 21), Employee("B", 22))
  list.groupBy(_.name).map { case (name, employes) =>
    employes.map(each => each.copy(age = each.age + 1))
  }.foreach(println)


  def longestSubString(str: String) = {
    val strList = str.split("").toList

    def innerMethod(innerStr: List[String], acc: Int = 0): Int = {
      innerStr match {
        case Nil => acc
        case ::(head, tl) =>
          val duplicateFilter = tl.filter(each => head == each)
          if (duplicateFilter.isEmpty) {
            innerMethod(tl, acc)
          } else {
            val unique = tl.diff(duplicateFilter)
            innerMethod(unique, acc + 1)
          }
      }
    }

    innerMethod(strList)
  }

  println("find count : " + longestSubString("abcabc"))

  }




  object LongestSubstringPointers {
    def longestUniqueSubstring(s: String): String = {
      var left = 0
      var right = 0
      var maxLen = 0
      var maxSubstr = ""
      val seen = scala.collection.mutable.Map[Char, Int]()

      while (right < s.length) {
        val char = s(right)
        // If char is already in the current window, move left pointer
        if (seen.contains(char) && seen(char) >= left) {
          left = seen(char) + 1
        }
        // Update the last seen index of the character
        seen(char) = right

        // Update max substring if current window is longer
        if (right - left + 1 > maxLen) {
          maxLen = right - left + 1
          maxSubstr = s.substring(left, right + 1)
        }

        right += 1  // Move the right pointer forward
      }

      maxSubstr
    }

    def main(args: Array[String]): Unit = {
      val input = "abcabcbb"
      println("Longest substring without repeating characters: " + longestUniqueSubstring(input))
    }

}

