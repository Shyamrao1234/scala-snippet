package zomatoordersproject.actors

/**
 * Created by Shyamrao on Mar 07, 2026.
 */


import akka.actor.typed._
import akka.actor.typed.scaladsl._
import zomatoordersproject.model.Order


object OrderGuardian {

  sealed trait Command

  case class CreateOrder(order: Order) extends Command

  case class GetOrder(id: String,
                      replyTo: ActorRef[Option[Order]]
                     ) extends Command

  case class UpdateStatus(id: String, status: String) extends Command

  case class GetAllOrder(replyTo: ActorRef[List[Order]]) extends Command


  def apply(): Behavior[Command] = Behaviors.setup { context =>

    var orderActor = Map.empty[String, ActorRef[OrderActor.Command]]

    def getOrCreate(orderId: String) = {
      orderActor.getOrElse(orderId, {
        val actor = context.spawn(OrderActor(), s"Order-${orderId}")
        orderActor += (orderId->actor)
        actor
      })
    }

    Behaviors.receiveMessage {
      case CreateOrder(order) => {
        val actor = getOrCreate(order.orderId)
        actor ! OrderActor.CreateOrder(order)
        Behaviors.same
      }

      case GetOrder(id, replyTo)    => {
        orderActor.get(id) match {
          case Some(actor) => actor ! OrderActor.GetOrder(replyTo)
          case None        => replyTo ! None
        }
        Behaviors.same
      }
      case UpdateStatus(id, status) =>
        orderActor.get(id).foreach(_ ! OrderActor.UpdateStatus(status))
        Behaviors.same
      case GetAllOrder(replyTo)     =>
        context.log.info("GetAllOrders called")
        replyTo ! List.empty
        Behaviors.same
    }
  }

}
