package akkatypedactors

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt

object UserSessionManager {

  sealed trait Command
  final case class Login(userId: String, replyTo: ActorRef[LoginResponse]) extends Command
  final case class Logout(userId: String, replyTo: ActorRef[LoginResponse]) extends Command
  final case class GetActiveSessions(replyTo:ActorRef[SessionCount]) extends Command

  final case class SessionEnded(userId:String) extends Command


  sealed trait LoginResponse
  case object LoginSuccess extends LoginResponse
  case object AlreadyLoggingIn extends LoginResponse

  final case class SessionCount(count:Int)


  object SessionManager {

    def apply():Behavior[Command]=active(Map())


    def active(sessions:Map[String,ActorRef[Session.Command]]):Behavior[Command] = Behaviors.receive{(context,message)=>
      message match {
        case Login(userId, replyTo) =>
          sessions.get(userId) match {
            case Some(value) =>
              replyTo ! AlreadyLoggingIn
              Behaviors.same
            case None =>
              val sessionActor = context.spawn(Session(userId,context.self), s"session-$userId")
              context.log.info(s"${userId} logged in")
              replyTo ! LoginSuccess
              active(sessions + (userId -> sessionActor))
          }
        case Logout(userId, replyTo) =>
          sessions.get(userId) match {
            case Some(session) =>
              context.stop(session)
              replyTo ! LoginSuccess
              active(sessions-userId)
            case None =>
              replyTo ! AlreadyLoggingIn
              Behaviors.same
          }
        case GetActiveSessions(replyTo) =>
          replyTo ! SessionCount(sessions.size)
          Behaviors.same
        case SessionEnded(userId) =>
          active(sessions - userId)
      }
    }
  }



  object Session {

    sealed trait Command
    private case object SessionTimeout extends Command


    def apply(userId:String,
              manager:ActorRef[UserSessionManager.Command]
             ):Behavior[Command] =
      Behaviors.withTimers{timer=>
        timer.startSingleTimer(SessionTimeout,30.seconds)

        Behaviors.receive{(context,message)=>
          message match {
            case SessionTimeout =>
              context.log.info(s"Session expired for userId : $userId")
              manager ! SessionEnded(userId)
              Behaviors.stopped
          }
        }
      }
  }



  def testUserManager():Unit = {
  val userGuardian:Behavior[Unit]= Behaviors.setup[Unit]{ context =>
    val userSessionManager=context.spawn(SessionManager(),"SessionManger")

    userSessionManager ! Login("user1",context.system.ignoreRef)
    userSessionManager ! Login("user2",context.system.ignoreRef)

    Thread.sleep(500)

    userSessionManager ! GetActiveSessions(context.system.ignoreRef)

    Thread.sleep(3500)

    userSessionManager ! GetActiveSessions(context.system.ignoreRef)

     Behaviors.empty
  }
    val system=ActorSystem(userGuardian,"user")
    Thread.sleep(20000)
    system.terminate()
  }


  def main(args: Array[String]): Unit = {
    testUserManager()
  }

}
