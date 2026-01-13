package akkastreams

import akka.actor.ActorSystem
import akka.stream.ThrottleMode
import akka.stream.impl.Throttle
import akka.stream.scaladsl.{Merge, Source}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object ThrottleExample extends App{

  implicit val system=ActorSystem("Throttle")
  import system.dispatcher

//  val res=Source(1 to 10)
//    .throttle(elements = 5,per = 1.seconds,10,ThrottleMode.Enforcing).runForeach(println)


//  res.onComplete {
//    case Failure(exception) => println("failed with exception "+exception)
//    system.terminate()
//    case Success(value) => println("successfull")
//      system.terminate()
//  }



//  val s1  =  Source(1 to 10)
//  val s2  =  Source(11 to 20)
//  val s3  =  Source(11 to 20)
//
//  Source.combine(s1,s2,s3)(Merge(_))
//


}
