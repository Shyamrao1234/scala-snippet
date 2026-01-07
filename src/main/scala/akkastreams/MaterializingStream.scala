package akkastreams

import akka.actor.{ActorSystem, actorRef2Scala}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object MaterializingStream extends App{

    implicit val system=ActorSystem("Materializing")

     import system.dispatcher

  //choosing materialized value
  val simpleSource=Source(1 to 10)
  val simpleFlow=Flow[Int].map(x=>x+1)
  val simpleSink = Sink.foreach[Int](println)
  val graph=simpleSource.viaMat(simpleFlow)(Keep.right)
    .toMat(simpleSink)(Keep.right)

  //sugar
//  Source(1 to 10).runWith(Sink.reduce(_+_)) //sorce.to(Sink.reduce(_+_))(keep.right).run
//  Source(1 to 10).runReduce(_+_)

  //Run component backword
  Sink.foreach[Int](println).runWith(Source.single(42))
  Flow[Int].map(x=>2*x).runWith(simpleSource,simpleSink)

  /***
   * -return the last element out of source (use Sink.last)
   * -compute the total word count of a stream of sentences
   * -map, fold,reduce
   */

    val sentenceSource=Source(List(
      "Akka is awesome",
      "I love strems",
      "Materialized value"
    ))


  val wordCounterSink=Sink.fold[Int,String](0)((acc,nextSentence)=>acc+nextSentence.split(" ").length)
  val g1=sentenceSource.toMat(wordCounterSink)(Keep.right).run()
  val g2= sentenceSource.runWith(wordCounterSink)
  val g3=sentenceSource.runFold[Int](0)((acc,nextSentence)=>acc+nextSentence.split(" ").length)

  val wordCountFlow=Flow[String].fold[Int](0)((acc,nextSentence)=>acc+nextSentence.split(" ").length)






  //  val factorials=Source(1 to 2).scan(BigInt(1))((acc,next)=>acc * next)
//
//  factorials.zipWith(Source(0 to 2))((num,idx)=>s"$idx!=$num")
//    .throttle(1,1.seconds)
//    .runForeach(println)





}

object findEachSentenceCount extends App{
  implicit val system=ActorSystem("Materializing")

  import system.dispatcher

  val sentenceSource=Source(List(
    "Akka is awesome",
    "I love strems",
    "Materialized value"
  ))

  sentenceSource.scan[Int](0)((acc,nextSentence)=> nextSentence.split(" ").length).runForeach(println)

}
