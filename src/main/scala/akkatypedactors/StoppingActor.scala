package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors

object StoppingActor {

  object SensitiveActor {

    def apply()=idle()

    def idle(): Behavior[String] = Behaviors.receive[String] { (context, message) =>
      context.log.info(s"Received ${message}")
      if (message == "you're ugly") {
        Behaviors.stopped(() => context.log.info("Stopping actor"))
      } else {
        Behaviors.same
      }
    }
  }

  def testSensitiveActor(): Unit = {
    val userGuardian = Behaviors.setup[Unit] { context =>
      val sensitiveActor = context.spawn(SensitiveActor(), "StoppingActor")
      context.watch(sensitiveActor)

      sensitiveActor ! "Hello this is rohan"
      sensitiveActor ! "Hello I am Rahul"
      sensitiveActor ! "you're ugly"
      sensitiveActor ! "sorry"
      sensitiveActor ! "sorry"


      Behaviors.receiveSignal[String] {
        case (context, Terminated(ref)) =>
          context.log.info(s"${ref.path.name} terminated")
          Behaviors.same
      }
      Behaviors.empty
    }
    val actorSystem = ActorSystem(userGuardian, "Stop")
    Thread.sleep(1000)
    actorSystem.terminate()
  }


  def main(args: Array[String]): Unit = {
    testSensitiveActor()
  }

}
