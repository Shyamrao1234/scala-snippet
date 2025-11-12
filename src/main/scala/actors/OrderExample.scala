import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

//package actors
//
//import akka.actor.typed.scaladsl.Behaviors
//import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
//
//private object  Assembler {
//
//  final case class BuildCar(model:String,replyTo:ActorRef[Build])
//  final case class Build(model:String,from:ActorRef[BuildCar])
//
//  def apply():Behavior[BuildCar] = Behaviors.receive{(context,message)=>
//    context.log.info("Assembling car model: {}",message.model)
//    message.replyTo ! Build(message.model,context.self)
//    Behaviors.same
//  }
//
//}
//
//private object Manager {
//
//  def apply(max: Int): Behavior[Assembler.Build] = manage(0, max)
//
//  private def manage(counter: Int, max: Int): Behavior[Assembler.Build] =
//    Behaviors.receive { (context, message) =>
//      val n = counter + 1
//      context.log.info(" Manager: Receive completed car #{} of model {}", n, message.model)
//
//      if (n == max) {
//        context.log.info(" Manger: Target of {} cars reached. Stopping.", max)
//        Behaviors.stopped
//      } else {
//        message.from ! Assembler.BuildCar(message.model, context.self)
//        manage(n, max)
//      }
//
//    }
//}
//
// private object FactoryController {
//
//    final case class StartProduction(model:String)
//
//    def apply():Behavior[StartProduction]= Behaviors.setup{context=>
//      val assembler =context.spawn(Assembler(),"assembler")
//
//      Behaviors.receiveMessage{message=>
//        val manager = context.spawn(Manager(max = 3),s"${message.model}-manager")
//        assembler ! Assembler.BuildCar(message.model,manager)
//        Behaviors.same
//      }
//    }
//
//  }
//
//private object TestActor extends App {
//  // ✅ Start the ActorSystem here
//  val system: ActorSystem[FactoryController.StartProduction] =
//    ActorSystem(FactoryController(), "car-factory")
//
//  // ✅ Send the first message to start everything
//  system ! FactoryController.StartProduction("Tesla")
//  system ! FactoryController.StartProduction("BMW")
//  system ! FactoryController.StartProduction("Audi")
//
//
//  // Optional: wait and terminate
//  Thread.sleep(3000)
//  system.terminate()
//}
//

sealed trait UserMessage
case class SendMessage(text:String) extends UserMessage
case object GoAway extends UserMessage
case object ComeBack extends UserMessage
case object Logout extends UserMessage

object CharUser {


   def online:Behavior[UserMessage] =Behaviors.receive{(context,message)=>{
     message match {
       case SendMessage(text)=> context.log.info(s"Sending message :${text}")
       Behaviors.same

       case GoAway =>
         context.log.info("user is away")
         away
       case Logout =>
         context.log.info("User logged out")
         Behaviors.stopped

       case ComeBack =>
         context.log.info("User came online")
         Behaviors.same
     }
   }}

  def away:Behavior[UserMessage] = Behaviors.receive{(context,message)=>
    message match {
      case SendMessage(text)=>
        context.log.info(s"Stopping message to send later: $text")
        Behaviors.same

      case ComeBack=>
        context.log.info("User is back online")
        online

      case GoAway =>
        Behaviors.same

      case Logout =>
        context.log.info("User logged out from away")
        Behaviors.stopped
    }}


  def main(args: Array[String]): Unit = {
    val system=ActorSystem(online,"CharUserSystem")

    system ! SendMessage("Hii")
    system ! GoAway
    system ! SendMessage("Hiii")
    system ! ComeBack
    system ! Logout
    system ! SendMessage("Hello")

  }



}

