package akkastreams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.Sorting

object FirstPrinciple extends App {

  implicit val actorSystem = ActorSystem("FirstPrinciple")
  implicit val materializer = ActorMaterializer

  //upstream => towards the source
  //downStream => towards the sink


  //various kinds or source
  val finiteSource = Source.single(Future(10))
  val futureSource = Source.fromFuture(Future(32))
  val emptySource = Source.empty[Int]
  val anotherFiniteSource = Source(List(1, 2, 3, 4))
  val infiniteSource = Source(Stream.from(1))
  val anotherFutureSource = Source.future(Future(10))
  val anotherFutureSource_1 = Source.futureSource(Future(Source(1 to 10)))


  //various kind of sink
  val theMostBoringSink = Sink.ignore
  val foreachSink = Sink.foreach[String](println)
  val headSink = Sink.head[Int]
  val foldSink = Sink.fold[Int, Int](0)((a, b) => a + b)


  //flow
  val mapFlow = Flow[Int].map(x => 2 * x)
  val takeFlow = Flow[Int].take(5)

  //syntatic sugar
  Source(1 to 10).runForeach(println)

  /**
   * Exercise : create a stream that takes the name of person, then you will keep the first 2 names with length > 5
   *
   * */
  case class Person(name: String)

  val sourceOfPerson = Source(List(Person("A"), Person("Shyamrao"), Person("C"), Person("Bhimrao")))
  sourceOfPerson.filter(_.name.length > 5).take(2).runForeach(println)

}


object FirstPrincipleRecap extends App {

  implicit val system = ActorSystem("FirstPrinciple")
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 100)
  val sink = Sink.foreach[Int](println)

  val graph = source.to(sink)
  graph.run()

  source.runWith(sink)

  //ways to create source.
  Source(List(1, 2, 3, 4))
  Source.single[Int](1)
  //  Source.single[String](null) // will throw null pointer exception
  Source.empty[Int] // returns nothing
  Source.maybe[Int]
  Source.future(Future(100))

  //throttle is used to make delay in the flow
  Source.fromIterator { () => Iterator.from(1) }.throttle(1, 1.seconds).to(Sink.foreach(println))

  //periodic sources
  val repeatSource=Source.repeat("tick") //emits tick indefinite
  val cycleSource=  Source.cycle(()=> List(1,2,3).iterator)//repeats sequence
  val tickSource=  Source.tick(0.seconds,1.seconds,"tick")


   // Source -> flow -> sink
    // syntactic sugar
  Source(1 to 10).map(x=> x * 2) // Source(1 to 10).via(Flow[Int].map(x=>x*2))
  Source(1 to 10).runForeach(println) //Source(1 to 10).to(Sink.foreach(println))

}