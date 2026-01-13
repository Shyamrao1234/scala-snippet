package akkatypedactors

import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}

import scala.util.Random

object RouterPoolExample {

  trait Command
  case class DoLog(text:String) extends Command

  object WorkerActor {
    def apply(counter:ActorRef[IncrementCommand]):Behavior[Command] =active(counter)


    def active(counter:ActorRef[IncrementCommand]):Behavior[Command]=
      Behaviors.receive {(context,message)=>
        message match {
          case DoLog(text) =>
            if(Random.nextInt(10)==2 || Random.nextInt(10)==4 || Random.nextInt(10)==8 ) throw new RuntimeException("Failed due >>>>>>>")
            counter ! Increment(text,context.self)
           Behaviors.same
        }
      }
  }

  trait IncrementCommand
  case class Increment(text:String,worker:ActorRef[Command]) extends IncrementCommand

  object IncrementActor {
    def apply():Behavior[IncrementCommand]= active()

    def active(count:Int=0):Behavior[IncrementCommand] = Behaviors.receive {(context,message)=>
      message match {
        case Increment(text,worker) =>
          context.log.info(s"[ProcessTask - ${worker.path.name}] Received text ${text} - ${count}")
          active(count+1)
      }
    }
  }

  def testPoolRouter() = {

    val userGuardian = Behaviors.setup[Unit]{context=>
      val incrementActor = context.spawn(IncrementActor(),"increment")
      val pool=Routers.pool(4){
        Behaviors.supervise(WorkerActor(incrementActor)).onFailure[Exception](SupervisorStrategy.restart)
      }

      val router = context.spawn(pool,"worker-pool")

      (1 to 10).foreach{n=>
        router ! DoLog("Hello Log")
      }

      Behaviors.empty
    }
    val actorSystem=ActorSystem(userGuardian,"user-guardian")
    Thread.sleep(1000)
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    testPoolRouter()
  }




}
