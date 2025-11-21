package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}


object ChatRoom {

  sealed trait RoomCommand
  final case class GetSession(screenName:String,replyTo:ActorRef[SessionEvent]) extends RoomCommand


  sealed trait SessionEvent
  final case class SessionGranted(handle:ActorRef[PostMessage]) extends SessionEvent
  final case class SessionDenied(reasion:String) extends SessionEvent
  final case class MessagePosted(screenName:String,message:String) extends SessionEvent

  sealed trait SessionCommand
  final case class PostMessage(message:String) extends SessionCommand
  private final case class NotifyClient(message:MessagePosted) extends SessionCommand
  private final case class PublishSessionMessage(screenName: String, message: String) extends RoomCommand


  def apply():Behavior[RoomCommand]=chatRoom(List.empty)

  private def chatRoom(sessions: List[ActorRef[SessionCommand]]): Behavior[RoomCommand] =
    Behaviors.receive { (context, message) =>
      message match {
        // when someone wants to join
        case GetSession(screenName, client) =>
          val sessionActor = context.spawn(
            session(context.self, screenName, client),
            name = s"session-$screenName"
          )
          client ! SessionGranted(sessionActor)
          chatRoom(sessionActor :: sessions)

        // when someone posts a message
        case PublishSessionMessage(screenName, message) =>
          val notification = MessagePosted(screenName, message)
          sessions.foreach(_ ! NotifyClient(notification))
          Behaviors.same
      }
    }


  private def session(
                       room: ActorRef[PublishSessionMessage],
                       screenName: String,
                       client: ActorRef[SessionEvent]
                     ): Behavior[SessionCommand] =
    Behaviors.receiveMessage {
      case PostMessage(message) =>
        // send message to room
        room ! PublishSessionMessage(screenName, message)
        Behaviors.same

      case NotifyClient(message) =>
        // forward message to the client
        client ! message
        Behaviors.same
    }
    




}

object Client {

  def apply(name: String): Behavior[ChatRoom.SessionEvent] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage {
        case ChatRoom.SessionGranted(handle) =>
          context.log.info(s"[$name] Session granted, sending greeting...")
          handle ! ChatRoom.PostMessage(s"Hello everyone, I'm $name!")
          Behaviors.same

        case ChatRoom.MessagePosted(screenName, message) =>
          context.log.info(s"[$name sees] $screenName: $message")
          Behaviors.same

        case ChatRoom.SessionDenied(reason) =>
          context.log.warn(s"[$name] Session denied: $reason")
          Behaviors.stopped
      }
    }
}


object ChatRoomApp extends App {

  // Create the chat room actor system
  val chatRoom = ActorSystem(ChatRoom(), "ChatRoomDemo")

  // Create two client actors
  val alice = chatRoom.systemActorOf(Client("Alice"), "client-Alice")
  val bob = chatRoom.systemActorOf(Client("Bob"), "client-Bob")

  // Both request sessions
  chatRoom ! ChatRoom.GetSession("Alice", alice)
  chatRoom ! ChatRoom.GetSession("Bob", bob)

  // Let the app run for a while before termination
  Thread.sleep(3000)
  chatRoom.terminate()
}




