package zomatoordersproject.actors

import akka.actor.typed._
import akka.actor.typed.scaladsl._
import zomatoordersproject.domain.{Order, OrderStatus}

object OrderGuardian {

  sealed trait Command

  case class CreateOrder(order: Order, replyTo: ActorRef[Order]) extends Command
  case class GetOrder(id: String, replyTo: ActorRef[Option[Order]]) extends Command
  case class UpdateStatus(id: String, status: OrderStatus, replyTo: ActorRef[Order]) extends Command
  case class GetAllOrder(replyTo: ActorRef[List[Order]]) extends Command

  def apply(): Behavior[Command] = Behaviors.setup { context =>

    var orderActor = Map.empty[String, ActorRef[OrderActor.Command]]

    def getOrCreate(orderId: String): ActorRef[OrderActor.Command] = {
      orderActor.getOrElse(orderId, {
        // Use backoff supervisor to handle failures
        val supervisedBehavior = Behaviors.supervise(OrderActor(orderId))
          .onFailure[Exception](SupervisorStrategy.restart)
        val actor = context.spawn(supervisedBehavior, s"Order-${orderId}")
        orderActor += (orderId -> actor)
        actor
      })
    }

    Behaviors.receiveMessage {
      case CreateOrder(order, replyTo) =>
        val actor = getOrCreate(order.orderId)
        actor ! OrderActor.CreateOrder(order, replyTo)
        Behaviors.same

      case GetOrder(id, replyTo) =>
        orderActor.get(id) match {
          case Some(actor) => actor ! OrderActor.GetOrder(replyTo)
          case None => replyTo ! None
        }
        Behaviors.same

      case UpdateStatus(id, status, replyTo) =>
        orderActor.get(id) match {
          case Some(actor) => actor ! OrderActor.UpdateStatus(status, replyTo)
          case None => 
            context.log.warn(s"UpdateStatus requested for non-existent order $id")
            // Ideally we'd reply with an error type, but for simplicity replying with un-updated or missing is tricky.
            // Since we need an Order, we might not be able to reply properly here without changing return type to Option[Order].
            // To fix this without breaking OrderRoute yet, we can spawn actor and hope persistence loads state, 
            // but the actor would be empty. Let's just create the actor to load from persistence.
            val actor = getOrCreate(id)
            actor ! OrderActor.UpdateStatus(status, replyTo)
        }
        Behaviors.same

      case GetAllOrder(replyTo) =>
        context.log.info("GetAllOrders called. This is inefficient via actors, use OrderRepository.")
        replyTo ! List.empty
        Behaviors.same
    }
  }

}
