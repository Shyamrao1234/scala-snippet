package akkatypedactors

import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt
import scala.util.Random


sealed trait SensorCommand

final case class ReadSensor(sensorId: String) extends SensorCommand

object SensorActor {

  def apply(): Behavior[SensorCommand] = active()

  def active(): Behavior[SensorCommand] = Behaviors.receive { (context, message) =>
    message match {
      case ReadSensor(sensorId) =>
        val random = new Random()
        context.log.info(s"[ $sensorId ] - Value : ${random.nextInt(100)} ")
        Behaviors.same
    }
  }
}

sealed trait TickCommand

case object Tick extends TickCommand

object SensorPoller {
  def apply(sensorIds: Seq[String]): Behavior[TickCommand] = Behaviors.withTimers { timer =>
    timer.startTimerAtFixedRate(Tick, 2.seconds)

    Behaviors.setup { context =>
      val sensors = sensorIds.map { id =>
        id -> context.spawn(SensorActor(), id)
      }.toMap


      Behaviors.receiveMessage {
        case Tick =>
          sensors.foreach { case (id, actorRef) =>
            actorRef ! ReadSensor(id)
          }
          Behaviors.same
      }
    }
  }
}


object SensorDataManagement {
  def testSensorManagement(): Unit = {
    val useGuardian: Behavior[Unit] =
      Behaviors.setup { context =>
        val sensorIds = Seq("MB00-1", "MB00-2", "MB00-3")
        val sensorPool = context.spawn(SensorPoller(sensorIds), "sensorPool")
        sensorPool ! Tick
        Behaviors.empty
      }
    val actorSystem = ActorSystem(useGuardian, "user")

//    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    testSensorManagement()
  }


}
