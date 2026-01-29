package leetcode

import org.joda.time.DateTime

import scala.annotation.tailrec

object DigitExtraction {

  def findDigitCount(n: Int) = {
    var counter = 0
    var x = n
    while (x > 0) {
      x = x / 10
      counter += 1
    }
    counter
  }

  @tailrec
  def reverseDigit(n: Int, rev: Int = 0): Int = {
    if (n == 0) rev
    else reverseDigit(n / 10, (rev * 10) + n % 10)
  }


  def isPalindrome(n: Int): Boolean = {
    reverseDigit(n) == n
  }


  def getArmStromNumber(n:Int,sum:Int=0):Int = {
    if(n==0) sum
    else getArmStromNumber(n/10,sum + ((n%10)*(n%10)*(n%10)))
  }

  def isArmStromNumber(n: Int): Boolean = {
    getArmStromNumber(n) == n
  }




  def main(args: Array[String]): Unit = {
    println(findDigitCount(3345))
    println("Reverse value 1234 , OutPut :"+reverseDigit(1234, 0))
    println("IsPalindrome : "+isPalindrome(122))

    println("Armstrong : "+isArmStromNumber(153))
  }

}
