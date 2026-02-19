package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt


sealed trait Command1

case object PaymentReceived extends Command1

case object PaymentTimeout extends Command1

object TimeoutActor {

  def apply(): Behavior[Command1] = Behaviors.withTimers { timer =>
    Behaviors.setup { context =>

      //schedule timeout
      timer.startSingleTimer(PaymentTimeout, PaymentTimeout, 10.seconds)

      Behaviors.receiveMessage {
        case PaymentReceived =>
          context.log.info("payment received in time")
          Behaviors.same
        case PaymentTimeout =>
          context.log.info("Payment time out")
          Behaviors.stopped
      }
    }
  }
}


object WithTimerBehaviour {

  def userGuardian() = {
    val userGuardian: Behavior[Unit] = Behaviors.setup[Unit] { context =>
      context.log.info("UserGuardian started")
      import context.executionContext

      val timeoutActor = context.spawn(TimeoutActor(), "timeOutActor")

      context.scheduleOnce(
        3.seconds,
        timeoutActor,
        PaymentReceived
      )
      Behaviors.empty
    }
    val actorSystem = ActorSystem(userGuardian, "user")
    Thread.sleep(20000)
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    userGuardian()
  }

}
