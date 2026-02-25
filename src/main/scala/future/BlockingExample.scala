package future

import scala.concurrent.{Future, blocking}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
 * Created by Shyamrao on Feb 24, 2026.
 */

object BlockingExample extends App {


    val f1 = Future{
       blocking{
         println("Blocking thread Name "+ Thread.currentThread().getName)
         Thread.sleep(20000)
       }
      println("Non blocking code " +Thread.currentThread().getName)
      10
    }

    f1.onComplete {
      case Failure(exception) => println("exception")
      case Success(value)     => println(value)
    }

    Thread.sleep(100000)


}
