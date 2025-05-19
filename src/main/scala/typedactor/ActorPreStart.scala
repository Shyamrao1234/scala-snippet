package typedactor


import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object ActorPreStart extends App {

  object StringActor {
    def apply(): Behavior[String] = Behaviors.setup { context =>
      println("do anything before actor start")
      Behaviors.receiveMessage[String] { message =>
        println("message received " + message)
        Behaviors.same
      }
    }
  }

  val actorPreStart=ActorSystem(StringActor(),"StringPreStart")

  actorPreStart ! "hello this is shyam"


}
