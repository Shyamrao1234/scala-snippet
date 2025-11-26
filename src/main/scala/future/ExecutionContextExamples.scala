package future

import java.util.concurrent.{Executor, Executors}
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

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


object CallBackExample extends App{

  import scala.concurrent.ExecutionContext.Implicits.global

  val result=Future{
    println("[My Future is called ]")
    Thread.sleep(1000)
    123
  }

  result.onComplete (println)

  Await.result(result,Duration.Inf)

}


/**
 * Difference between foreach and onComplete
 *
 * foreach will not handle failure
 * onComplete will handle failure
 *
 * */

object ForOnComplete extends App{

  import scala.concurrent.ExecutionContext.Implicits.global
  val res=Future{
    println("Future Executing.....")
    10/0
  }

  res.foreach(each=>println(each))

//  res.onComplete {
//    case Failure(exception) => exception
//    case Success(value) => value
//  }

   Await.result(res,Duration.Inf)

}


/**
 * Example to know on which thread the call back runs
 * */

object CallBackThread extends App{

  import scala.concurrent.ExecutionContext.Implicits.global

  val future=Future{
    println("[Thread Name ].."+Thread.currentThread().getName)
    println("Future Executing......")
    123
  }


  Thread.sleep(500)

  future.onComplete {
    case Failure(exception) => println("")
  case Success(value) => println("[Thread Name of callback1]..."+Thread.currentThread().getName)
  }

  future.onComplete {
    case Failure(exception) => println("")
    case Success(value) => println("[Thread Name of callback2]..."+Thread.currentThread().getName)
  }

  Thread.sleep(1000)

}

object  FutureFilter extends App{
  import scala.concurrent.ExecutionContext.Implicits.global

  val future=Future{
   2
  }

 for{
   x<- future if(x==10)
 }yield x


//  Await.result(res,1000.seconds)
//
//  res.onComplete(println)


}