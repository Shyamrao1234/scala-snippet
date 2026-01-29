package akkastreams.graphs

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}

import scala.concurrent.duration.DurationInt

object GraphBasicExamples extends App {

  implicit val system = ActorSystem("GraphBasics")
  implicit val materializer = ActorMaterializer()


  /** *
   * One source and many sink
   */

  val graph1 = RunnableGraph.fromGraph { // wrap the graph
    GraphDSL.create() { implicit builder => //builds the graph
      import GraphDSL.Implicits._

      val inputSource = Source(1 to 1000)
      val sink1 = Sink.foreach[Int](x => println("Sink - 1 " + x))
      val sink2 = Sink.foreach[Int](x => println("Sink - 2 " + x))

      val broadCast = builder.add(Broadcast[Int](2))
      inputSource ~> broadCast

      broadCast.out(0) ~> sink1
      broadCast.out(1) ~> sink2


      ClosedShape
    }
  }

  //  graph1.run()


  /** *
   * two source and one sink
   */

  val graph2 = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val source1 = Source(1 to 100)
      val source2 = Source(1 to 100)

      val sink = Sink.foreach[Int](println)

      val merge = builder.add(Merge[Int](2))
      source1 ~> merge
      source2 ~> merge
      merge ~> sink

      ClosedShape
    }
  )

  //  graph2.run()


  /** *
   *
   * One source , two flow and one sink
   */


  val graph3 = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val source = Source(1 to 100)
      val flow1 = Flow[Int].map { x => println("Flow 1 : " + (x + 1)); x }
      val flow2 = Flow[Int].map { x => println("Flow 2 : " + (x * 2)); x }
      val sink = Sink.ignore

      //declare components
      val broadcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))

      source ~> broadcast
      broadcast.out(0) ~> flow1 ~> merge.in(0)
      broadcast.out(1) ~> flow2 ~> merge.in(1)

      merge ~> sink


      ClosedShape
    }
  )


  //  graph3.run()


  /**
   * Balance
   */

  val graph4 = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import  GraphDSL.Implicits._
      val fastSource = Source(1 to 1000).throttle(10, 1.seconds)
      val slowSource = Source(1 to 1000).throttle(1, 1.seconds)

      val sink1 = Sink.foreach[Int](x => println("Sink-1 " + x))
      val sink2 = Sink.foreach[Int](x => println("Sink-2 " + x))

      val merge = builder.add(Merge[Int](2))
      val balance = builder.add(Balance[Int](2))

      fastSource ~> merge ~> balance ~> sink1
      slowSource ~> merge
      balance ~> sink2

      ClosedShape
    }
  )


   graph4.run()
}
