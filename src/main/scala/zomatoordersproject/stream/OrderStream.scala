package zomatoordersproject.stream

import akka.actor.typed.ActorSystem
import akka.stream.scaladsl._
import akka.stream._
import zomatoordersproject.domain.Order
import akka.NotUsed

object OrderStream {

  /**
   * Creates a publish-subscribe engine for real-time order processing.
   * Returns a queue to push new/updated orders into, and a Source that
   * multiple clients (e.g. Server-Sent Events, WebSockets) can attach to.
   */
  def createStream(implicit system: ActorSystem[_]): (SourceQueueWithComplete[Order], Source[Order, NotUsed]) = {
    Source.queue[Order](1000, OverflowStrategy.backpressure)
      .toMat(BroadcastHub.sink[Order](bufferSize = 256))(Keep.both)
      .run()
  }

}
