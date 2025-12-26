package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors


object NotificationCentreSystem {


  trait Command
  case class Notify(msg:String) extends Command

  object NotificationActor{
    def apply():Behavior[Command] = Behaviors.receive  {(context,message)=>
      message match {
        case Notify(msg)=>
          context.log.info("I have received a notification "+msg)
          Behaviors.same
      }
    }
  }


  def testNotificationSystem() = {
    val userGuardian:Behavior[Unit]= Behaviors.setup{context=>
      val notificationActor=context.spawn(NotificationActor(),"NotificationSystem")
      notificationActor ! Notify("Hello This is akka")
      Behaviors.empty
    }

    val actorSystem=ActorSystem(userGuardian,"user")
    Thread.sleep(1000)
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    testNotificationSystem()
  }

}
