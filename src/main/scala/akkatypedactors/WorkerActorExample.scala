package akkatypedactors

import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}

import scala.concurrent.duration.DurationInt
import scala.util.Random

object WorkerActorExample {


  sealed trait Command

  case class ProcessTask(id:Int,replyTo:ActorRef[Response]) extends Command
  case object FailNow extends Command

  sealed trait Response
  case class TaskSuccess(id:Int) extends Response
  case class TaskFailed(id:Int,reason:String) extends Response


  object WorkerActor {

    def apply():Behavior[Command] = Behaviors.supervise(active()).onFailure[RuntimeException](
      SupervisorStrategy.restartWithBackoff(
        minBackoff = 1.seconds,
        maxBackoff = 5.seconds,
        randomFactor = 0.2
      )
    )

    private def active():Behavior[Command]=Behaviors.receive{(ctx,message)=>
      message match {
        case ProcessTask(id, replyTo) => {
          ctx.log.info(s"[TaskId - ${id}] Process the given task.......")
          if(id%3==0) throw  new RuntimeException("Simulated Failed")
          else {
            replyTo ! TaskSuccess(id)
            Behaviors.same
          }
        }
        case FailNow => ctx.log.error("Worker failed forcefully")
        Behaviors.same
      }
    }
  }


  object ServerActor {
    def apply(workPoolSize:Int):Behavior[Command] = Behaviors.setup{context=>

      val workerPool=context.spawn(
        Routers.pool(poolSize = workPoolSize)(Behaviors.supervise(WorkerActor()).onFailure[Exception](SupervisorStrategy.restart)),
        "woorkPool"
      )

      Behaviors.receiveMessage {
          case msg @ ProcessTask(id, replyTo) =>
            context.log.info(s"Server: received task ${id}")
            //forward to pool
            workerPool ! msg
            Behaviors.same
          case FailNow =>
            context.log.info("Server : instructing worker to fail")
            Behaviors.same
      }
    }
  }





  def main(args: Array[String]): Unit = {
    val system: ActorSystem[Command] = ActorSystem(ServerActor(workPoolSize = 4), "userGuardian")

    import system.executionContext
    import scala.concurrent.duration._

    system.scheduler.scheduleAtFixedRate(2.seconds, 2.seconds) { () =>
      val id = Random.nextInt(100)
      system ! ProcessTask(id, system.ignoreRef)
    }

    // send an initial task
    system ! ProcessTask(1, system.ignoreRef)
  }


}
