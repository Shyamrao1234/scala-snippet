package typedactor

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Example1 extends App {

  object StringActor {
    def apply(): Behavior[String] = Behaviors.receive[String] {
      (context, message) =>
        println("received a message " + message)
        Behaviors.same
    }
  }

  val actorSystem = ActorSystem(StringActor(), "StringActor")
  actorSystem ! "hello this your dad"


}
