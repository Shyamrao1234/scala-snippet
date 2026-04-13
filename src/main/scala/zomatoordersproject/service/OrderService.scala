package zomatoordersproject.service

import akka.actor.typed.Scheduler
import zomatoordersproject.actors.{OrderGuardian}
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import zomatoordersproject.model.Order

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

/**
 * Created by Shyamrao on Mar 14, 2026.
 */

class OrderService(orderActor: ActorRef[OrderGuardian.Command])(implicit scheduler: Scheduler,ec:ExecutionContext) {


  implicit val timeout: Timeout = Timeout(2.seconds)


  def createOrder(order: Order) = {
    orderActor ! OrderGuardian.CreateOrder(order)
  }

  def getOrderById(orderId: String) = {
    orderActor.ask(OrderGuardian.GetOrder(orderId, _))
  }

  def getAllOrders() = {
    orderActor.ask(OrderGuardian.GetAllOrder(_))
  }

  def updateStatus(orderId: String, status: String) = {
    orderActor ! OrderGuardian.UpdateStatus(orderId, status)
  }

}
