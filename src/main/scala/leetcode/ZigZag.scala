package leetcode


object ZigzagConversion {
  def convert(s: String, numRows: Int): String = {
    // Edge cases: if rows are 1 or string is too short, return original
    if (numRows <= 1 || numRows >= s.length) return s

    // Create a StringBuilder for each row
    val rows = Array.fill(numRows)(new StringBuilder)

    var curRow = 0
    var goingDown = false

    // Iterate over each character and put it in the correct row
    for (ch <- s) {
      rows(curRow).append(ch)
      // Change direction when we hit top or bottom row

      if (curRow == 0 || curRow == numRows - 1) goingDown = !goingDown
      curRow += (if (goingDown) 1 else -1)
    }

    // Combine all rows into final result
    rows.mkString
  }

  def main(args: Array[String]): Unit = {
    val input = "PAYPALISHIRING"
    val numRows = 3
    val result = convert(input, numRows)
    println(s"Input: $input")
    println(s"Rows: $numRows")
    println(s"Output: $result") // Should print PAHNAPLSIIGYIR
  }
}

object Dmo extends App {


  def convert(s: String, numRows: Int): String = {
    if (numRows == 1 || numRows >= s.length) s

    var curvRow = 0
    var goingDown = false

    val rows = Array.fill(numRows)(new StringBuilder)
    for (ch <- s) {
      rows(curvRow).append(ch)

      if (curvRow == 0 || curvRow == numRows - 1) goingDown = !goingDown

      curvRow += (if (goingDown) 1 else -1)
    }

    println(rows.foreach(println))
    rows.mkString
  }

  println(convert("PAYPALISHIRING", 3))
  println("outPut = PAHNAPLSIIGYIR")

}