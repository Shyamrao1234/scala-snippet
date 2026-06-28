package leetcode

object FindFirstNonRepeatingChar extends App {


  private def execute(str: String): Option[Char] = {
    val map = scala.collection.mutable.Map.empty[Char, Long]
    for (i <- str.indices) {
      map(str(i)) = map.getOrElse(str(i), 0L) + 1L
    }

    for (i <- str.indices) {
      if (map(str(i)) == 1) {
        return Some(str(i))
      }
    }

    None
  }

  println(execute("swiss"))

}
