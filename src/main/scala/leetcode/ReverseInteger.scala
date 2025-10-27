package leetcode

object ReverseInteger extends App {


  def reverse(x: Int): Int = {

    var rev: Long = 0
    var num = x
    while (num != 0) {
      var digit = num % 10
      rev = rev * 10 + digit
      num = num / 10
    }
    if (rev < Int.MinValue || rev > Int.MaxValue) 0 else rev.toInt
  }


  println(reverse(1534236469))

  println(Int.MaxValue)

}
