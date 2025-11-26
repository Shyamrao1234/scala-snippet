package future

import java.util.concurrent.{Executors, ThreadFactory}
import scala.concurrent.{ExecutionContext, Future}

object DurationExample extends App{

  val threadFactory:ThreadFactory= (r:Runnable)=>{
    val t=new Thread(r)
    t.setDaemon(true)
    t
  }

  val executor=Executors.newFixedThreadPool(1,threadFactory)
  implicit  val ex=ExecutionContext.fromExecutor(executor)


  Future {
    Thread.sleep(500)
    println("[Thread Name-1]"+Thread.currentThread().getName)
    123
  }.onComplete(println)

  Future {
    Thread.sleep(500)
    println("[Thread Name-2]"+Thread.currentThread().getName)
    234
  }.onComplete(println)


  Thread.sleep(2000)

//  executor.shutdown()

  println("[Thread Name Main ]"+Thread.currentThread().getName)

}
