package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy, Terminated}
import akka.actor.typed.scaladsl.Behaviors

object Supervision {


  object ParentActor {

    def apply():Behavior[String]=Behaviors.setup{context=>
      val  child = context.spawn(FussyWordCounter(),"fussyChild")
      context.watch(child)

      Behaviors.receiveMessage[String]{message=>
        child ! message
        Behaviors.same
      }.receiveSignal{
        case (context,Terminated(childRef)) =>
          context.log.warn(s"Child failed : ${childRef}")
          Behaviors.same
      }
    }
  }


  object ParentActorWithSupervisor {

    def apply():Behavior[String] = Behaviors.setup{context=>
      val childBehavior =Behaviors.supervise(FussyWordCounter())
        .onFailure[RuntimeException](SupervisorStrategy.resume)

      val child=context.spawn(childBehavior,"Supervisor")
      context.watch(child)

      Behaviors.receiveMessage[String]{message=>
        child ! message
        Behaviors.same
      }.receiveSignal{
        case (context,Terminated(childRef)) =>
          context.log.warn(s"Child failed : ${childRef}")
          Behaviors.same
      }
    }
    }


  object FussyWordCounter {

    def apply():Behavior[String]= active()

    def active(totalCount:Int=0) : Behavior[String] =Behaviors.receive{(context,message)=>
      val wordCount=message.split(" ").length
      context.log.info(s"Received piece of test : ${message}, counted $wordCount words, total : ${totalCount+wordCount}")

      //throw exception
      if(message.startsWith("Q")) throw new RuntimeException("I have Q")
      if(message.startsWith("W")) throw new NullPointerException

      active(totalCount+wordCount)
    }
  }


  def demoCrash(): Unit = {
   val guardian:Behavior[Unit] = Behaviors.setup{context=>
     val fussyActor=context.spawn(ParentActorWithSupervisor(),"FussyWord")
     fussyActor !  "Hello akka actor"
     fussyActor ! "Quick"
     fussyActor ! "Are you there"
     Behaviors.empty
   }
    val system=ActorSystem(guardian,"userGuardian")
    Thread.sleep(1000)
    system.terminate()
  }



  def main(args: Array[String]): Unit = {
    demoCrash()
  }
}
