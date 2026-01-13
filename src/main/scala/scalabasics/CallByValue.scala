package scalabasics

import org.joda.time.DateTime


object CallByValue extends App{


  def callByName(x: => Long = System.currentTimeMillis()) = {
    val start=x
    Thread.sleep(100)
    println(x - start)
  }

  println(callByName())
}
