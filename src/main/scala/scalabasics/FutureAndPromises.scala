package scalabasics


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.util.{Failure, Success}

object FutureAndPromises extends App{

  val promise= Promise[String]

  Future{
    Thread sleep 1000
    promise.success("You've just completed the promise with me in it!")
  }

  promise.future.onComplete {
    case Failure(exception) => println(exception)
    case Success(value) => println(value)
  }

}


object FutureExample extends App{

  def log(msg:String) = println(s"[${Thread.currentThread().getName}] $msg")

  def task(name:String,delay:Int) = {
    log(s"Starting $name")
    Thread.sleep(delay) // simulate some work
    log(s"Finished ${name}")
    delay
  }


  def timeIt[A](label: String)(block: => A): A = {
    val start = System.nanoTime()
    val result=block
    val end = System.nanoTime()
    log(s"${label} took ${end-start/1e6} ms\n" )
    result
  }

  timeIt("Sequential run"){
    val sequential =for{
      a<- Future(task("Task A", 1000))
      b<- Future(task("Task B", 1000))
    }yield (a+b)
  }







}