package akkatypedactors

import akka.actor.typed.{ActorSystem}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akkatypedactors.OrderProcessingSystem.OrderActor.PlaceOrder

object OrderProcessingSystem {


  sealed trait PaymentResponse
  case object PaymentSuccess extends PaymentResponse
  case object  PaymentFailed extends PaymentResponse

  object PaymentActor {

    sealed trait Command
    case class Pay(orderId: String, replyTo: ActorRef[PaymentResponse]) extends Command
    case class Fail(orderId:String, replyTo:ActorRef[PaymentResponse]) extends Command


    def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
        case Pay(orderId, replyTo) =>
          context.log.info(s"[payment] Payment processing for order ${orderId}")
          replyTo ! PaymentSuccess
          Behaviors.same

        case Fail(orderId,replyTo)=>
          context.log.info(s"[payment] Payment failed for order ${orderId}")
          replyTo ! PaymentFailed
          Behaviors.same
      }
    }
  }


  sealed trait InventoryResponse
  case object StockAvailable extends InventoryResponse
  case object OutOfStock extends InventoryResponse

  object InventoryActor {

    trait Command
    case class CheckStock(orderId: String, replyTo: ActorRef[InventoryResponse]) extends Command

    val products = Map("NikeShoes"->3,"AirCooler"->2)

    def apply(): Behavior[Command] = active(products)

    def active(products:Map[String,Int]):Behavior[Command] =Behaviors.receive {(context,message)=>
      message match {
        case CheckStock(orderId,replyTo)=>
          products.get(orderId) match {
            case Some(count) => {
              replyTo ! StockAvailable
              context.log.info(s"[Inventory] Only ${count - 1} ${orderId} are left")
              if (count == 1) active(products - (orderId)) else active(products + (orderId -> (count - 1)))
            }
            case None => {
              replyTo ! OutOfStock
              Behaviors.same
            }
          }
      }
    }
  }


  object OrderActor{

    sealed trait Command
    case class PlaceOrder(orderId:String) extends Command
    case class  WrappedPayment(orderId:String,res:PaymentResponse) extends Command
    case class WrappedInventory(orderId:String,res:InventoryResponse) extends  Command

    def apply(): Behavior[Command] = Behaviors.setup { context =>

      val paymentActor = context.spawn(PaymentActor(), "Payment")
      val inventoryActor = context.spawn(InventoryActor(), "Inventory")



      import PaymentActor._
      import InventoryActor._

      Behaviors.receiveMessage { message =>
        message match {
          case PlaceOrder(orderId) =>
            context.log.info(s"[Order] Places order $orderId")
            val inventoryAdapter: ActorRef[InventoryResponse] = context.messageAdapter(res => WrappedInventory(orderId,res))
            inventoryActor ! CheckStock(orderId, inventoryAdapter)
            Behaviors.same
          case WrappedPayment(orderId,PaymentSuccess) =>
            context.log.info(s"[order] payment successful for orderId ${orderId}")
            Behaviors.same
          case WrappedPayment(orderId,PaymentFailed) =>
            context.log.info(s"[order] payment failed for orderID ${orderId}")
            Behaviors.same
          case WrappedInventory(orderId,StockAvailable) =>
            val paymentAdapter: ActorRef[PaymentResponse] = context.messageAdapter(res => WrappedPayment(orderId,res))
            paymentActor ! Pay(orderId,paymentAdapter)
            context.log.info("[order] stock available")
            Behaviors.same

          case WrappedInventory(orderId,OutOfStock) =>
            context.log.info("[Order] Out of stock")
            val paymentAdapter: ActorRef[PaymentResponse] = context.messageAdapter(res => WrappedPayment(orderId,res))
            paymentActor ! Fail(orderId,paymentAdapter)
            Behaviors.same
        }
      }
    }
  }

  def testOrderManagementSystem(): Unit = {
    val userGuardian:Behavior[Unit]=Behaviors.setup{context=>
      val orderActor=context.spawn(OrderActor(),"PlaceOrder")
      orderActor ! PlaceOrder("NikeShoes")
      orderActor ! PlaceOrder("NikeShoes")
      orderActor ! PlaceOrder("NikeShoes")
      orderActor ! PlaceOrder("NikeShoes")
      Behaviors.empty
    }

    val actorSystem=ActorSystem(userGuardian,"UserGuardian")
    Thread.sleep(1000)
    actorSystem.terminate()
  }


  def main(args: Array[String]): Unit = {
    testOrderManagementSystem()
  }
}
