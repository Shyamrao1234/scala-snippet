package akkastreams.graphs

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

object OpenGraphs extends App {

  implicit val system = ActorSystem("OpenGraphs")
  implicit val materializer = ActorMaterializer()


  val source1 = Source(1 to 20)
  val source2 = Source(30 to 90)
  val sink = Sink.foreach(println)

  source1.concat(source2).runWith(sink)
}
