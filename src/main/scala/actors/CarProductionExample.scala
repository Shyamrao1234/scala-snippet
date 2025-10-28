package actors

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

// --------------------- Assembler ---------------------
private object Assembler {
  final case class BuildCar(model: String, replyTo: ActorRef[Build])
  final case class Build(model: String, from: ActorRef[BuildCar])

  def apply(): Behavior[BuildCar] = Behaviors.receive { (context, message) =>
    context.log.info("Assembling car model: {}", message.model)
    message.replyTo ! Build(message.model, context.self)
    Behaviors.same
  }
}

// --------------------- Manager ---------------------
private object Manager {
  def apply(max: Int, supervisor: ActorRef[Supervisor.CarCompleted]): Behavior[Assembler.Build] =
    manage(0, max, supervisor)

  private def manage(counter: Int, max: Int, supervisor: ActorRef[Supervisor.CarCompleted])
  : Behavior[Assembler.Build] =
    Behaviors.receive { (context, message) =>
      val n = counter + 1
      context.log.info("Manager: Completed car #{} of model {}", n, message.model)

      // Notify the supervisor each time a car is done
      supervisor ! Supervisor.CarCompleted(message.model)

      if (n == max) {
        context.log.info("Manager: Target of {} cars reached for model {}. Stopping.", max, message.model)
        Behaviors.stopped
      } else {
        message.from ! Assembler.BuildCar(message.model, context.self)
        manage(n, max, supervisor)
      }
    }
}

// --------------------- Supervisor ---------------------
private object Supervisor {
  final case class StartFactory(globalTarget: Int, models: List[String])
  final case class CarCompleted(model: String)

  def apply(): Behavior[Any] = Behaviors.setup { context =>
    val assembler = context.spawn(Assembler(), "assembler")
    running(context, assembler, totalProduced = 0, globalTarget = 0)
  }

  private def running(
                       context: ActorContext[Any],
                       assembler: ActorRef[Assembler.BuildCar],
                       totalProduced: Int,
                       globalTarget: Int
                     ): Behavior[Any] =
    Behaviors.receiveMessage {
      case StartFactory(target, models) =>
        context.log.info("Supervisor: Starting factory for models: {}", models)
        models.foreach { model =>
          val manager = context.spawn(Manager(max = target, context.self), s"${model}-manager")
          assembler ! Assembler.BuildCar(model, manager)
        }
        running(context, assembler, totalProduced, target)

      case CarCompleted(model) =>
        val newTotal = totalProduced + 1
        context.log.info("Supervisor: Received car #{} (Model: {})", newTotal, model)

        if (newTotal >= globalTarget && globalTarget > 0) {
          context.log.info("Supervisor: Global target of {} cars reached! Stopping factory.", globalTarget)
          Behaviors.stopped
        } else {
          running(context, assembler, newTotal, globalTarget)
        }
    }
}

// --------------------- Test ---------------------
private object TestActor extends App {
  val system: ActorSystem[Any] = ActorSystem(Supervisor(), "smart-factory")

  system ! Supervisor.StartFactory(
    globalTarget = 3, // Global total across all models
    models = List("Tesla", "BMW", "Audi")
  )

  Thread.sleep(5000)
  system.terminate()
}
