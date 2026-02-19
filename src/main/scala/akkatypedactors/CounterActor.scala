package akkatypedactors

import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}


object CounterActor {

  sealed trait CounterCommand

  case object Increment extends CounterCommand

  case object Decrement extends CounterCommand

  case class GetValue(replyTo: ActorRef[Result]) extends CounterCommand

  case class Result(value: Int)

  def apply(): Behavior[CounterCommand] = active()

  def active(value: Int = 0): Behavior[CounterCommand] = Behaviors.receive { (context, message) =>
    message match {
      case Increment         =>
        val newValue = value + 1
        context.log.info("Incremented value to " + newValue)
        active(newValue)
      case Decrement         =>
        val newValue = value - 1
        context.log.info("Decremented value to " + newValue)
        active(newValue)
      case GetValue(replyTo) =>
        replyTo ! Result(value)
        Behaviors.same
    }
  }
}

object Manager {


  trait ManagerCommand

  case object CreateCounter extends ManagerCommand

  case object IncrementCounter extends ManagerCommand

  case object GetValue extends ManagerCommand

  case class WrappedCounterResponse(response: CounterActor.Result) extends ManagerCommand

  case object CounterRequestFailed extends ManagerCommand

  def apply(): Behavior[ManagerCommand] = Behaviors.setup { context =>
    implicit val timeout: Timeout = 3.seconds
    implicit val scheduler        = context.system.scheduler
    implicit val ex               = context.executionContext

    var counterOpt: Option[ActorRef[CounterActor.CounterCommand]] = None
    Behaviors.receiveMessage {
      case CreateCounter    =>
        val counterActor = context.spawn(CounterActor(), "Counter")
        counterOpt = Some(counterActor)
        Behaviors.same
      case IncrementCounter =>
        counterOpt.foreach(_ ! CounterActor.Increment)
        Behaviors.same
      case GetValue         =>
        counterOpt.foreach { counter =>
          context.ask(counter, (replyTo: ActorRef[CounterActor.Result]) =>
            CounterActor.GetValue(replyTo)
          ) {
            case Success(response) => WrappedCounterResponse(response)
            case Failure(_)        => CounterRequestFailed
          }
        }
        Behaviors.same

      case WrappedCounterResponse(result) =>
        context.log.info("Result : " + result)
        Behaviors.same
      case CounterRequestFailed =>
        context.log.info("Failed with exception")
        Behaviors.same
    }
  }
}

object MainExecutor extends App {

  val actorSystem = ActorSystem(Manager(), "manager")

  actorSystem ! Manager.CreateCounter
  actorSystem ! Manager.IncrementCounter
  actorSystem ! Manager.IncrementCounter
  actorSystem ! Manager.IncrementCounter
  actorSystem ! Manager.IncrementCounter
  actorSystem ! Manager.GetValue

}
