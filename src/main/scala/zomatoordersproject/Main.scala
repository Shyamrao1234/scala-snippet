package zomatoordersproject

/**
 * Created by Shyamrao on Mar 07, 2026.
 */


import akka.actor.typed._
import akka.actor.typed.scaladsl._
import zomatoordersproject.actors._
import zomatoordersproject.http._
import zomatoordersproject.service.OrderService

object Main {

  def main(args: Array[String]): Unit = {

    val root = Behaviors.setup[Nothing] { context =>
      implicit val system    = context.system
      implicit val scheduler = context.system.scheduler
      implicit val ec        = context.system.executionContext
      
      val orderRepository = new zomatoordersproject.repository.InMemoryOrderRepository()
      val orderGuardian = context.spawn(OrderGuardian(), "order-manager")
      val (queue, source) = zomatoordersproject.stream.OrderStream.createStream(context.system)
      val routes        = new OrderRoutes(new OrderService(orderGuardian, orderRepository, queue))
      HttpServer.start(context.system, routes)
      Behaviors.empty
    }
    ActorSystem[Nothing](root, "OrderSystem")
  }

}