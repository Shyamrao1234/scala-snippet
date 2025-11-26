package future

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object DurationExample extends App{

  val executor=Executors.newFixedThreadPool(1)
  implicit  val ex=ExecutionContext.fromExecutor(executor)

  Future {
    Thread.sleep(2000)
    println("[Thread Name-1]"+Thread.currentThread().getName)
    123
  }.onComplete(println)

  Future {
    Thread.sleep(2000)
    println("[Thread Name-2]"+Thread.currentThread().getName)
    234
  }.onComplete(println)



//  executor.shutdown()

  println("[Thread Name Main ]"+Thread.currentThread().getName)

}
