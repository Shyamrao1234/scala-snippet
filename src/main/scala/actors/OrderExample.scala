package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

object  Assembler {

  final case class BuildCar(model:String,replyTo:ActorRef[Build])
  final case class Build(model:String,from:ActorRef[BuildCar])

  def apply():Behavior[BuildCar] = Behaviors.receive{(context,message)=>
    context.log.info("Assembling car model: {}",message.model)
    message.replyTo ! Build(message.model,context.self)
    Behaviors.same
  }

}

object Manager {

  def apply(max: Int): Behavior[Assembler.Build] = manage(0, max)

  private def manage(counter: Int, max: Int): Behavior[Assembler.Build] =
    Behaviors.receive { (context, message) =>
      val n = counter + 1
      context.log.info(" Manager: Receive completed car #{} of model {}", n, message.model)

      if (n == max) {
        context.log.info(" Manger: Target of {} cars reached. Stopping.", max)
        Behaviors.stopped
      } else {
        message.from ! Assembler.BuildCar(message.model, context.self)
        manage(n, max)
      }

    }
}

  object FactoryController {

    final case class StartProduction(model:String)

    def apply():Behavior[StartProduction]= Behaviors.setup{context=>
      val assembler =context.spawn(Assembler(),"assembler")

      Behaviors.receiveMessage{message=>
        val manager=context.spawn(Manager(max = 3),s"${message.model}-manager")
        assembler ! Assembler.BuildCar(message.model,manager)
        Behaviors.same
      }

    }

  }

object TestActor extends App {
  // ✅ Start the ActorSystem here
  val system: ActorSystem[FactoryController.StartProduction] =
    ActorSystem(FactoryController(), "car-factory")

  // ✅ Send the first message to start everything
  system ! FactoryController.StartProduction("Tesla")

  // Optional: wait and terminate
  Thread.sleep(3000)
  system.terminate()
}

