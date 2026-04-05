package akkatypedactors

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

import akka.actor.Cancellable
import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt

object SchedulerDemo {


  object LoggerActor {
    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"Received message ${message}")
      Behaviors.same
    }
  }

  def demoScheduler() = {
    val userGuardian = Behaviors.setup[Unit] { context =>
      val loggerActor = context.spawn(LoggerActor(), "loggerActor")
      context.log.info("Process started")
      context.scheduleOnce(1.seconds, loggerActor, "Hello this is shyam")
      Behaviors.empty
    }

    val system = ActorSystem(userGuardian, "userGuardian")
    Thread.sleep(2000)
    system.terminate()
  }


  object ResettingTimeoutActor {

    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"Received ${message}")
      resettingTimeoutActor(context.scheduleOnce(1.seconds, context.self, "timeout"))
    }

    def resettingTimeoutActor(scheduler: Cancellable): Behavior[String] = Behaviors.receive { (context, message) =>
      message match {
        case "timeout" =>
          context.log.info("Stopping")
          Behaviors.stopped

        case _ =>
          context.log.info(s"Received ${message}")
          scheduler.cancel()
          resettingTimeoutActor(context.scheduleOnce(1.seconds, context.self, "timeout"))
      }

    }
  }


  def demoResettingTimeout() = {
    val userGuardian = Behaviors.setup[Unit] { context =>
      val resettingActor = context.spawn(ResettingTimeoutActor(), "resetActor")
      resettingActor ! "start timer"
      Thread.sleep(500)
      resettingActor ! "reset"
      Thread.sleep(700)
      resettingActor ! "this should still be visible"

      Behaviors.empty
    }

    val system = ActorSystem(userGuardian, "userGuardian")
    import system.executionContext
    system.scheduler.scheduleOnce(4.seconds, () => system.terminate())
  }


  def main(args: Array[String]): Unit = {
    demoResettingTimeout()
  }


}
