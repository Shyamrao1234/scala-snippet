package zomatoordersproject.actors

import zomatoordersproject.domain._

import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}

object OrderActor {

  sealed trait Command

  case class GetOrder(replyTo: ActorRef[Option[Order]]) extends Command
  case class UpdateStatus(status: OrderStatus, replyTo: ActorRef[Order]) extends Command
  case class CreateOrder(order: Order, replyTo: ActorRef[Order]) extends Command

  sealed trait Event
  case class OrderCreated(order: Order) extends Event
  case class StatusUpdated(status: OrderStatus) extends Event

  def apply(orderId: String): Behavior[Command] =
    Behaviors.setup { context =>
      EventSourcedBehavior[Command, Event, Option[Order]](
        persistenceId = PersistenceId.ofUniqueId(orderId),
        emptyState = None,
        commandHandler = (state, command) => handleCommand(orderId, state, command, context),
        eventHandler = (state, event) => handleEvent(state, event)
      )
    }

  private def handleCommand(orderId: String, state: Option[Order], command: Command, context: ActorContext[Command]): Effect[Event, Option[Order]] = {
    command match {
      case CreateOrder(order, replyTo) =>
        state match {
          case Some(_) =>
            context.log.warn(s"Order $orderId already created.")
            Effect.none.thenReply(replyTo)(_ => order) // Alternatively, reply with an error
          case None =>
            Effect.persist(OrderCreated(order)).thenReply(replyTo)(_ => order)
        }

      case GetOrder(replyTo) =>
        Effect.none.thenReply(replyTo)(_ => state)

      case UpdateStatus(status, replyTo) =>
        state match {
          case Some(order) =>
            Effect.persist(StatusUpdated(status)).thenReply(replyTo)(newState => newState.get)
          case None =>
            context.log.warn(s"Order $orderId not found for update.")
            Effect.none // Depending on requirement, we could reply with an error here
        }
    }
  }

  private def handleEvent(state: Option[Order], event: Event): Option[Order] = {
    event match {
      case OrderCreated(order) => Some(order)
      case StatusUpdated(status) => state.map(_.withStatus(status))
    }
  }
}
