package http

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


object DirectivesDemo extends App {

  //  val route: Route = { ctx => ctx.compl?ete("yeah") }


}

object parallelism extends App {

  def task(name: String) = {
      Future{
          println(s"Task $name started on thread ${Thread.currentThread().getName}")
          Thread.sleep(2000) // Simulate time-consuming work
          println(s"Task $name finished")
      }
  }

    val a=task("A")
    val b=task("B")

    Await.result(Future.sequence(Seq(a,b)),Duration.Inf)






}