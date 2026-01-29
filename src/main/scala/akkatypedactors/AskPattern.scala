package akkatypedactors


import akka.actor.typed
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, scaladsl}
import akka.util.Timeout
import akkatypedactors.OrderActor.PlaceOrder

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

//object AskPattern {
//
//}

object InventoryActor {
  sealed trait Command

  final case class CheckStock(itemId: String, replyTo: ActorRef[Boolean]) extends Command

  def apply(): Behavior[Command] = Behaviors.receiveMessage {
    case CheckStock(itemId, replyTo) =>
      val isStock = itemId == "item1"
      replyTo ! isStock
      Behaviors.same
  }
}

object OrderActor {
  sealed trait Command

  final case class PlaceOrder(itemId: String) extends Command

  private final case class WrappedStockResponse(itemId: String, inStock: Boolean) extends Command

  private final case class StockFailed(itemId: String, reason: Throwable) extends Command

  def apply(inventory: ActorRef[InventoryActor.Command]): Behavior[Command] = Behaviors.setup { context =>
    implicit val timeout: Timeout = 3.seconds
    implicit val scheduler = context.system.scheduler
    import context.executionContext

    Behaviors.receiveMessage {
      case PlaceOrder(itemId) =>
        //ask inventory if the item is in stock
        context.pipeToSelf(inventory.ask[Boolean](replyTo => InventoryActor.CheckStock(itemId, replyTo))) {
          case Success(inStock) => WrappedStockResponse(itemId, inStock)
          case Failure(ex) => StockFailed(itemId, ex)
        }
        Behaviors.same

      case WrappedStockResponse(itemId, true) =>
        context.log.info(s"Order confirmed for $itemId ✅")
        Behaviors.same

      case WrappedStockResponse(itemId, false) =>
        context.log.info(s"Order failed for $itemId ❌ Out of stock")
        Behaviors.same

      case StockFailed(itemId, ex) =>
        context.log.error(s"Inventory check failed for $itemId: ${ex.getMessage}")
        Behaviors.same
    }
  }
}

object MainAsk {

  def askGuardian(): Unit = {
    val userGuardian = Behaviors.setup[Unit] { context =>
      val inventory = context.spawn(InventoryActor(), "inventory")
      val orderActor = context.spawn(OrderActor(inventory), "PlaceOrder")
      orderActor ! OrderActor.PlaceOrder("item1")
      orderActor ! OrderActor.PlaceOrder("item2")
      Behaviors.empty
    }
    val system = ActorSystem(userGuardian, "userGuardian")
    Thread.sleep(1000)
    system.terminate()
  }


  def main(args: Array[String]): Unit = {
    askGuardian()
  }

}

object WeatherActor {
  sealed trait Command


  case class GetTemperature(city: String, replyTo: ActorRef[Option[Double]]) extends Command

  val map = Map("Bidar" -> 32.3, "Delhi" -> 21.5)


  def apply(): Behavior[Command] = Behaviors.receiveMessage {
    case GetTemperature(city, replyTo) =>
      val getTemp = map.get(city)
      replyTo ! getTemp
      Behaviors.same
  }
}


object AskWeather {
  sealed trait Command

  case class WhatIsTheWeather(city: String) extends Command

  case class Response(temperature: Option[Double]) extends Command

  case class Failed(exception: Throwable) extends Command


  def apply(weatherActor: ActorRef[WeatherActor.Command]): Behavior[Command] = Behaviors.setup { context =>
    implicit val timeout: Timeout = 3.seconds
    implicit val scheduler = context.system.scheduler
    import context.executionContext

    Behaviors.receiveMessage[Command] {
      case WhatIsTheWeather(city) =>
        context.pipeToSelf(
          weatherActor.ask[Option[Double]](replyTo =>
            WeatherActor.GetTemperature(city, replyTo))) {
          case Success(response) => Response(response)
          case Failure(ex) => Failed(ex)
        }
        Behaviors.same
      case Response(temp) =>
        context.log.info(s"Temperature $temp")
        Behaviors.same
      case Failed(ex) =>
        context.log.info(s"Failed with exception ${ex}")
        Behaviors.same
    }
  }


}