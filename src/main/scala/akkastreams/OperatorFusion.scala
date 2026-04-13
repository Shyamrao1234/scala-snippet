package akkastreams

import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import sun.java2d.pipe.SpanShapeRenderer.Simple

import scala.concurrent.Future

object OperatorFusion extends App {

  //Goal is to know understand how stream component running on same actor
  // how to introduce async boundaries between stream component

  implicit val actorSystem = ActorSystem("OperatorFusion")
  implicit val materiablizer = ActorMaterializer()

  //this will run on multiple dispatcher thread but sequentially
  //  Source(1 to 1000000).runForeach(x=>println(Thread.currentThread().getName +"-"+x))

  //To run in parallel // ordering is maintained
  //  Source(1 to 10000).mapAsync(8)(x=>Future.successful(Thread.currentThread().getName+" _ "+ x)).runForeach(println)

  //run in parallel, but ordering is not maintained
  //  Source(1 to 10000).mapAsyncUnordered(8)(x=>Future.successful(Thread.currentThread().getName+" _ "+ x)).runForeach(println)


  //this below code will run on same actor
  //  Source(1 to 1000).via(Flow[Int].map(x => x + 1)).to(Sink.foreach(println))


  //  object simpleActor extends Actor {
  //    override def receive: Receive = {
  //      case x: Int =>
  //        val x1 = x + 1
  //        println(x1)
  //    }
  //  }
  //
  //  val actor = actorSystem.actorOf(Props[SimpleActor])
  //  // more or less above code will work like this
  //  (1 to 100).foreach(x => actor ! x)


  //example where stream component run on multiple actors
  val complexFlow_1 = Flow[Int].map { x =>
    Thread.sleep(1000)
    x + 1
  }

  val complexFlow_2 = Flow[Int].map { x =>
    Thread.sleep(1000)
    x * 10
  }

  //If you see this code will take time to run, since this will run on same actor
  //  Source(1 to 10).via(complexFlow_1).via(complexFlow_2).runForeach(println)

  //here is the example where you can run the stream component on multiple actor
  Source(1 to 10)
    .via(complexFlow_1).async // from there stream component run on another actorRefWithActor
    .via(complexFlow_2).async //from here stream component run on another actor
    .runForeach(println)

  //guaranteed the ordering
  Source(1 to 3)
    .map { x => println("Element A - " + x); x }
    .map { x => println("Element B - " + x); x }
    .map { x => println("Element C - " + x); x }
    .to(Sink.ignore)

  // Will guaranteed the ordering of each actor
  Source(1 to 3)
    .map { x => println("Element A - " + x); x }.async
    .map { x => println("Element B - " + x); x }.async
    .map { x => println("Element C - " + x); x }.async
    .to(Sink.ignore)
}
