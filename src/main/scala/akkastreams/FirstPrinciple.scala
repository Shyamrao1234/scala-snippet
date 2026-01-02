package akkastreams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object FirstPrinciple extends App{

//  implicit  val system=ActorSystem("FirstPrinciple")
//  implicit val materializer= ActorMaterializer
//
//  val source=Source(1 to 10)
//  val sink=Sink.foreach(println)
//
//  val graph=source.to(sink)
//  graph.run()
//
//  val flow=Flow[Int].map(_ + 1)
//  source.via(flow).to(sink)

  Stream.from(1).map{x=>
    Thread.sleep(2000)
    x
  }.foreach(println)

}
