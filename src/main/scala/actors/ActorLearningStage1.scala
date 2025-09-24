package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.{Actor, ActorSystem, Props}


class ActorLearningStage1


class MyActor extends Actor {

  override def receive: Receive = {
    case "test" => println("test received")
    case _ => println("unknown msg sent")
  }

}

object Main extends App {

  val actorSystem = ActorSystem("myActor")

  val myActorRef = actorSystem.actorOf(Props[MyActor], "myActor")

  myActorRef ! "test"

  Thread.sleep(1000)
  actorSystem.terminate()


}


object HelloWorld {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])

  final case class Greeted(whom: String, from: ActorRef[Greet])

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    context.log.info("Hello {}!", message.whom)
    message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }
}

object HelloWorldBot {
  def apply(max: Int): Behavior[HelloWorld.Greeted] = {
    bot(0, max)
  }

  private def bot(greetingCounter: Int, max: Int): Behavior[HelloWorld.Greeted] = {
    Behaviors.receive { (context, message) =>
      val n = greetingCounter + 1
      context.log.info("Greeting {} for {}", n, message.whom)
      if (n == max) {
        Behaviors.stopped
      } else {
        message.from ! HelloWorld.Greet(message.whom, context.self)
        bot(n, max)
      }
    }
  }
}


object HelloWorldMain {

  final case class SayHello(name: String)

  def apply(): Behavior[SayHello] = Behaviors.setup { context =>
    val greeter = context.spawn(HelloWorld(), "greeter")


    Behaviors.receiveMessage { message =>
      val replyTo = context.spawn(HelloWorldBot(max = 3), message.name)
      greeter ! HelloWorld.Greet(message.name, replyTo)
      Behaviors.same
    }
  }


}
