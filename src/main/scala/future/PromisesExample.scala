package future

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

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
