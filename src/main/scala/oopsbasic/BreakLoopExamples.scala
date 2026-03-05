package oopsbasic

/**
 * Created by Shyamrao on Mar 03, 2026.
 */


/**
 * In java we use break to stop the loop. lets see what we use for scala
 */
object BreakLoopExamples extends App {

  import scala.util.control.Breaks._

  breakable {
    for (i <- 0 to 10) {
      if (i == 5) break()
      println(i)
    }
  }

}
