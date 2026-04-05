package zomatoordersproject.stream

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

import akka.actor.typed.ActorSystem
import akka.stream.scaladsl._
import akka.stream._

object OrderStream {

  def start(system: ActorSystem[_]): Unit = {

    implicit val sys = system
    implicit val mat = Materializer(sys)

    Source(1 to 1000000)
      .map(x => s"event-$x")
      .buffer(1000, OverflowStrategy.backpressure)
      .runForeach(e => println(e))
  }

}
