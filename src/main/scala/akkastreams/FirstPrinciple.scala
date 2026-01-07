package akkastreams

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FirstPrinciple extends App {

  implicit val actorSystem = ActorSystem("FirstPrinciple")
  implicit val materializer = ActorMaterializer


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
  val takeFlow=Flow[Int].take(5)

  //syntatic sugar
  Source(1 to 10).runForeach(println)
  /**
   * Exercise : create a stream that takes the name of person, then you will keep the first 2 names with length > 5
   *
   * */
  case class Person(name:String)
  val sourceOfPerson=Source(List(Person("A"),Person("Shyamrao"),Person("C"),Person("Bhimrao")))
  sourceOfPerson.filter(_.name.length > 5).take(2).runForeach(println)

}
