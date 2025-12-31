//package akkatypedactors
//
//import akka.actor.typed.scaladsl.Behaviors
//import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
//
//object ChatRoomSystem {
//
//
//  object ChatRoomManager {
//
//    sealed  trait Command
//    final case class CreateRoom(roomId:String,replyTo:ActorRef[ActionResponse]) extends Command
//    final case class JoinRoom(roomId:String,)
//
//
//
//    sealed trait ActionResponse
//
//
//  }
//
//}
