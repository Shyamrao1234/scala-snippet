package future

import java.util.concurrent.{Executor, Executors}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

class ExecutionContextExamples {}

/**
 * There are three types you can use Execution context
 *
 * global -----
 * custom -- Executor to ExecutionContext
 * Creating own execution context
 *
 * */

object GlobalContext extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val result1 = Future {
    println("[Global Thread] Thread Name-----" + Thread.currentThread().getName)
    Thread.sleep(1000)
    123
  }
  val result2 = Future {
    println("[Global Thread] Thread Name-----" + Thread.currentThread().getName)
    Thread.sleep(1000)
    123
  }
  Thread.sleep(2000)
  Await.result(result1, 2000.seconds)
  Await.result(result2, 2000.seconds)
}


object ExecutionExample2 extends App {

  val executor = Executors.newFixedThreadPool(1)
  implicit  val ec= ExecutionContext.fromExecutor(executor)


  lazy val result2 = Future {
    println("[Global Thread] Thread Name-----" + Thread.currentThread().getName)
    Thread.sleep(1000)
    123
  }

  Future.sequence(List(result2,result2)).onComplete{_=>
    executor.shutdown()
  }

}


class CustomExecution extends ExecutionContext{

  override def execute(runnable: Runnable): Unit = {
    println("Scheduling on CustomExecution")
    new Thread(runnable).start()
  }

  override def reportFailure(cause: Throwable): Unit = {
    println("Failed thread message"+cause.getMessage)
  }
}



object Custom extends App{

  implicit val ec: CustomExecution = new CustomExecution

  val result= Future{
    println("Thread Name "+Thread.currentThread().getName)
    123
  }

  result.foreach(res=> println(res))


}
