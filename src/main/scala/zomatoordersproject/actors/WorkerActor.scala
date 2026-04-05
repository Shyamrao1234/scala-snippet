package zomatoordersproject.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

object WorkerActor {


  trait Command
  case class ProcessOrder(orderId: String) extends Command


  def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
    message match {
      case ProcessOrder(orderId) =>
        context.log.info(s"Processing order ${orderId}")
        Behaviors.same
    }
  }


}


