package zomatoordersproject.actors

/**
 * Created by Shyamrao on Mar 07, 2026.
 */


import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.util.Timeout
import zomatoordersproject.model._

import scala.concurrent.duration.DurationInt

object OrderManager {

  sealed trait Command

  case class CreateOrder(order: Order) extends Command

  case class GetAllOrders(replyTo: ActorRef[List[Order]]) extends Command

  case class GetOrder(id: String, replyTo: ActorRef[Option[Order]]) extends Command

  case class UpdateStatus(id: String, status: String) extends Command


  implicit val timeout: Timeout = Timeout(2.seconds)

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>

      var orders = Map.empty[String, Order]

      Behaviors.receiveMessage {
        case CreateOrder(order) =>
          orders += (order.orderId -> order)
          Behaviors.same

        case GetOrder(id, replyTo) =>
          replyTo ! orders.get(id)
          Behaviors.same


        case GetAllOrders(reply) =>
          reply ! orders.values.toList
          Behaviors.same

        case UpdateStatus(id, status) =>
          orders.get(id).foreach(order =>
            orders += (order.orderId -> order.copy(status = status))
          )
          Behaviors.same
      }
    }
}
