package zomatoordersproject.actors

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

import zomatoordersproject.model._

import akka.actor.typed._
import akka.actor.typed.scaladsl._

object OrderActor {

  sealed trait Command

  case class GetOrder(replyTo: ActorRef[Option[Order]]) extends Command

  case class UpdateStatus(status: String) extends Command

  case class CreateOrder(order: Order) extends Command


  def apply(): Behavior[Command] =
    Behaviors.setup { context =>

      var state: Option[Order] = None
      Behaviors.receiveMessage {
        case CreateOrder(order) =>
          context.log.info(s"Order created ${order.orderId}")
          state = Some(order)
          Behaviors.same

        case GetOrder(replyTo) =>
          replyTo ! state
          Behaviors.same

        case UpdateStatus(status) =>
          state = state.map(_.copy(status = status))
          Behaviors.same
      }
    }
}
