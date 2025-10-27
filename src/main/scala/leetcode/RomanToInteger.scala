package leetcode

object RomanToInteger extends App {

  def romanToInt(roman: String) = {
    //
    //    val symbol = List("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
    //
    //    val values = List(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)

    val map = Map(
      'M' -> 1000,
      'D' -> 500,
      'C' -> 100,
      'L' -> 50,
      'X' -> 10,
      'V' -> 5,
      'I' -> 1
    )
    var total = 0
    for (i <- 0 until  roman.length) {
      val value = map(roman(i))
      if (i + 1 < roman.length && value < map(roman(i + 1))) {
        total -= value
      } else {
        total += value
      }
    }
    total
  }

 println( romanToInt("XXIV"))

}
