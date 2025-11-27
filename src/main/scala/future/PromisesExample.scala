package future

import java.util.concurrent.Executors
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object PromisesExample extends App{

  val executor=Executors.newFixedThreadPool(4)
  implicit val ec=ExecutionContext.fromExecutor(executor)


  val p=Promise[Int]()
  val f=p.future

  def produceSomething() = {
    println("[Waiting for 1 seconds] To produce data [Thread Name]"+Thread.currentThread().getName)
    Thread.sleep(1000)
    12
  }

  def doSomethingUnrelated() = {
    println("[Doing Something Unrelated for 2 Seconds]-----[Thread Name]"+Thread.currentThread().getName)
    Thread.sleep(2000)
    23
  }

  val producer=Future{
    val produce=produceSomething()
    p.success(produce)
    doSomethingUnrelated()
  }

  val consumer=Future{
    f.foreach(println)
    doSomethingUnrelated()
  }

}

/**
 * If we assign data to completed promise it will fail
 * */


object PromisesExample2 extends App{

  val promise=Promise[Int]
  val f=promise.future

  promise.success(11)
  promise.success(11) // will throw illegal state exception

}

object PromisesExample3 extends App{


  val futures =for(i<-1 to 5) yield {
    val promise=Promise[Int]
    val future=promise.future
    promise.success(i)
    future
  }

  Future{
    futures.map{f=>
      f.foreach(println)
    }
  }



}


object FutureExample43  extends App{
  val result=Future{
    Thread.sleep(500)
    throw new RuntimeException("No value present")
  }

  val res=Await.result(result,1000.seconds)

}
