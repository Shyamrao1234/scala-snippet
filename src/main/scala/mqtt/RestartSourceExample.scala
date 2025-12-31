package mqtt

import akka.actor.ActorSystem
import akka.stream.scaladsl.{RestartSource, Sink, Source}

import scala.concurrent.duration.DurationInt
import scala.util.Random

object RestartSourceExample  extends App{

  implicit  val system=ActorSystem("RestartSource")
  implicit val ec=system.dispatcher


  var i=0

  val unStableSource = Source.tick(0.seconds,1.seconds,()).map{_=>
    i+=1
    if(Random.nextInt(10)==0) throw new RuntimeException("Random failed")
    else s"Hello-${i}"
  }

  val restartableSource=RestartSource.onFailuresWithBackoff(
    1.seconds,5.seconds,0
  ){()=>
    unStableSource
  }

  restartableSource.runWith(Sink.foreach(println))

}
