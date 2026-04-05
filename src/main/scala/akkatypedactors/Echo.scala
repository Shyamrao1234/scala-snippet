package akkatypedactors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem}

/**
 * Created by Shyamrao on Mar 20, 2026.
 */

object Echo {

  case class Ping(message: String, response: ActorRef[Pong])

  case class Pong(message: String)

  def apply() = Behaviors.receiveMessage[Ping] {
    case Ping(message, replyTo) =>
      replyTo ! Pong(message)
      Behaviors.same
  }

}
