package scalabasics

import scala.annotation.tailrec

object Recursion extends App{


  @tailrec
  def factorial(n:Int,acc:Int):Int = {
    if(n<=1) acc else factorial(n-1,acc*n)
  }

  @tailrec
  def concatString(str:String,acc:String,n:Int): String = {
    if(n<=0) acc else concatString(str,acc+str,n-1)
  }


  def isPrime(n:Int):Boolean={
    @tailrec
    def isPrimeTailRec(t:Int,isStillPrime:Boolean):Boolean={
     if(!isStillPrime) false
     else if(t<=1) true
     else isPrimeTailRec(t-1,n%2!=0 && isStillPrime)
    }

    isPrimeTailRec(n/2,true)
  }


  def isPrime_V1(n:Int) = {
    n>1 && (2 until n).forall(n % _ != 0)
  }


  def isPrime_V3(n:Int): Boolean = {
    if(n<=1)false
    else if(n==2) true
    else if(n%2==0) false
    else {
      def checkDivisor(divisor:Int): Boolean = {
        if(divisor * divisor > n) true
        else if(n % divisor==0) false
        else checkDivisor(divisor + 2)
      }
     checkDivisor(3)
    }
  }





}
