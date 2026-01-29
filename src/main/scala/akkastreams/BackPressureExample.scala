package akkastreams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.duration.DurationInt

object BackPressureExample extends App {


  implicit val system = ActorSystem("BackPressure")
  implicit val materiablizer = ActorMaterializer()

  val fastSource = Source(1 to 1000)
  val slowSink = Sink.foreach[Int] { x =>
    Thread.sleep(1000)
    println(s"Sink : ${x}")
  }

  val flow = Flow[Int].map { x => println("Incoming value - " + x); x }

  fastSource.via(flow).async // this is actual backpressure
    .to(slowSink).async
  //    .run()


  //OverflowStrategy
  Source(1 to 1000)
    .buffer(10, OverflowStrategy.backpressure).async
    .map { x =>
      Thread.sleep(200)
      println(s"Processed order ${x}")
      x
    }.async
//    .runWith(Sink.ignore)


  Source.tick(0.seconds, 100.millis, "price-update")
    .buffer(3, OverflowStrategy.dropHead)
    .map { x =>
      Thread.sleep(500)
      println(s"Showing ${x}")
    }
    .runWith(Sink.ignore)

}
