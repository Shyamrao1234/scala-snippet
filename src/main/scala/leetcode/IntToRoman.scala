package leetcode

object IntToRoman extends App {

  def intToRoman(num: Int): String = {
    var num2 = num
    val symbol = List("M", "CM", "D", "CD",
      "C", "XC", "L", "XL",
      "X", "IX", "V", "IV",
      "I")
    val values = List(1000, 900, 500, 400,
      100, 90, 50, 40,
      10, 9, 5, 4,
      1
    )
    val stringBuilder = new StringBuilder()
    for (i <- 0 to symbol.length - 1) {
      while(num2 >= values(i)) {
        num2 -=  values(i)
        stringBuilder.append(symbol(i))
      }
    }
    stringBuilder.toString()
  }

  println(intToRoman(3749))

}
