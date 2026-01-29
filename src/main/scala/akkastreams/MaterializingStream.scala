package akkastreams

import akka.actor.{ActorSystem, actorRef2Scala}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object MaterializingStream extends App {

  implicit val system = ActorSystem("Materializing")

  import system.dispatcher

  //choosing materialized value
  val simpleSource = Source(1 to 10)
  val simpleFlow = Flow[Int].map(x => x + 1)
  val simpleSink = Sink.foreach[Int](println)
  val graph = simpleSource.viaMat(simpleFlow)(Keep.right)
    .toMat(simpleSink)(Keep.right)

  //sugar
  //  Source(1 to 10).runWith(Sink.reduce(_+_)) //sorce.to(Sink.reduce(_+_))(keep.right).run
  //  Source(1 to 10).runReduce(_+_)

  //Run component backword
  Sink.foreach[Int](println).runWith(Source.single(42))
  Flow[Int].map(x => 2 * x).runWith(simpleSource, simpleSink)

  /** *
   * -return the last element out of source (use Sink.last)
   * -compute the total word count of a stream of sentences
   * -map, fold,reduce
   */

  val sentenceSource = Source(List(
    "Akka is awesome",
    "I love strems",
    "Materialized value"
  ))


  val wordCounterSink = Sink.fold[Int, String](0)((acc, nextSentence) => acc + nextSentence.split(" ").length)
  val g1 = sentenceSource.toMat(wordCounterSink)(Keep.right).run()
  val g2 = sentenceSource.runWith(wordCounterSink)
  val g3 = sentenceSource.runFold[Int](0)((acc, nextSentence) => acc + nextSentence.split(" ").length)

  val wordCountFlow = Flow[String].fold[Int](0)((acc, nextSentence) => acc + nextSentence.split(" ").length)






  //  val factorials=Source(1 to 2).scan(BigInt(1))((acc,next)=>acc * next)
  //
  //  factorials.zipWith(Source(0 to 2))((num,idx)=>s"$idx!=$num")
  //    .throttle(1,1.seconds)
  //    .runForeach(println)


}

object findEachSentenceCount extends App {
  implicit val system = ActorSystem("Materializing")

  import system.dispatcher

  val sentenceSource = Source(List(
    "Akka is awesome",
    "I love strems",
    "Materialized value"
  ))

  sentenceSource.scan[Int](0)((acc, nextSentence) => nextSentence.split(" ").length).runForeach(println)

}


object MaterializingRecap extends App {

  implicit val system = ActorSystem("MaterializingValue")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher


  val simpleGraph = Source(1 to 100).to(Sink.foreach(println))
  val simpleGraphValue = simpleGraph.run() //Materializing value


  val source = Source(1 to 10)
  val sink = Sink.reduce[Int]((a, b) => a + b)
  val sumFuture = source.to(sink).run()
  val sumFuture_2 = source.runWith(sink) // source.toMat(sink)(Keep.right)
  sumFuture_2.onComplete {
    case Success(value) => println("Sum of stream is : " + value)
    case Failure(exception) => println("Stream failed with " + exception)
  }

  val simpleSource = Source(1 to 100)
  val simpleFlow = Flow[Int].map(x => x + 1)
  val simpleSink = Sink.foreach[Int](println)
  val graph = simpleSource.viaMat(simpleFlow)(Keep.right).toMat(simpleSink)(Keep.right)
  graph.run().onComplete {
    case Success(_) => println("Stream finished with done")
    case Failure(ex) => println("exception")
  }

  //sugar
  Source(1 to 10).runWith(Sink.reduce[Int](_+_))
  Source(1 to 10).runReduce(_ + _)


  /***
   * - return the last element out of the source (use Sink.last)
   * - compute the total word count out of a stream of sentence
   * -map, fold ,reduce
   *
   */

  val f1=Source(1 to 10).toMat(Sink.last)(Keep.right).run()
  val f2=Source(1 to 10).runWith(Sink.last)

  val sentenceWord = Source(List(
    "akka is awesome",
    "I love stream",
    "Materialzed values are killing me"
  ))

  val wordCountSink=Sink.fold[Int,String](0)((acc,str)=>acc+str.split(" ").length)
  val g1= sentenceWord.runWith(wordCountSink)
  val g2=sentenceWord.toMat(wordCountSink)(Keep.right)
  val g3= sentenceWord.fold[Int](0)((acc,str)=>acc+str.split(" ").length)



}