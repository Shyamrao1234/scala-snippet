package akkastreams.graphs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}

import scala.concurrent.duration.DurationInt

object GraphsBasics extends App {

  implicit val actorSystem = ActorSystem("BasicGraphs")
  implicit val actorMateriablizer = ActorMaterializer()

  val input = Source(1 to 1000)
  val incrementer = Flow[Int].map { x => x + 1 } // hard computation
  val multiplier = Flow[Int].map { x => x * 10 } //hard computation
  val outPut = Sink.foreach[(Int, Int)](println)


  val graph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] => // builder is a mutable data structure
      import GraphDSL.Implicits._

      //step -2 add necessary component to this graph
      val broadcast = builder.add(Broadcast[Int](2))
      val zip = builder.add(Zip[Int, Int])

      //step -3 tying up the components
      input ~> broadcast

      broadcast.out(0) ~> incrementer ~> zip.in0
      broadcast.out(1) ~> multiplier ~> zip.in1

      zip.out ~> outPut

      //step- 4 return a closed shapes
      ClosedShape
    }
  )

  //  graph.run()

  /**
   * exercise -> feed a source into 2 sinks at a same time
   */

  val firstSink = Sink.foreach[Int] { x => println("First Sink : " + x) }
  val secondSink = Sink.foreach[Int] { x => println("Second Sink : " + x) }

  val graph_1 = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      //create a necessary component
      val broadcast = builder.add(Broadcast[Int](2))

      //step 3 - feed to the broadcast
      input ~> broadcast

      broadcast.out(0) ~> firstSink
      broadcast.out(1) ~> secondSink

      ClosedShape
    }
  )

  //  graph_1.run()


  /** *
   * exercise 2 : balance
   */
  val fastSource = input.throttle(5, 1.seconds)
  val slowSource = input.throttle(2, 1.seconds)

  val sink1 = Sink.fold[Int, Int](0) { (count, _) =>
    println(s"Sink 1 number of elements ${count}")
    count + 1
  }

  val sink2 = Sink.fold[Int, Int](0) { (count, _) =>
    println(s"Sink 2 number of elements ${count}")
    count + 1
  }

  val balanceGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      //Step 2 -- declare components
      val merge = builder.add(Merge[Int](2))
      val balance = builder.add(Balance[Int](2))

      //Step 3 - tying components
      fastSource ~> merge ~> balance ~> sink1
      slowSource ~> merge
      balance ~> sink2

      ClosedShape
    }
  )


  balanceGraph.run()
}
