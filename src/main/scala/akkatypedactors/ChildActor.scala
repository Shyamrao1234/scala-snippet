package akkatypedactors

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.DurationInt

object ChildActor {


  object Parent {

    trait Command

    case class CreateChild(name: String) extends Command

    case class TellChildActor(message: String) extends Command


    def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
        case CreateChild(name) =>
          context.log.info(s"[Parent] creating child actor with name ${name}")
          val childActor: ActorRef[String] = context.spawn(Child(), name)
          active(childActor)
      }
    }

    def active(actorRef: ActorRef[String]): Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
        case TellChildActor(message) =>
          context.log.info(s"[Parent] sending message to ${message} to child")
          actorRef ! message
          Behaviors.same

        case _ =>
          context.log.info("[parent] command not supported")
          Behaviors.same
      }
    }
  }

  object Child {
    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"[Child] Received ${message} from Parent")
      Behaviors.same
    }
  }


  def callParentActor() = {
    import Parent._
    val parentActor = ActorSystem(Parent(), "Parent")
    parentActor ! CreateChild("Child")
    parentActor ! TellChildActor("Hey hpw are ?")
    Thread.sleep(1000)
    parentActor.terminate()
  }


  def main(args: Array[String]): Unit = {
    callParentActor()
  }


}

object Child {
  def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
    context.log.info(s"[${context.self.path.name}] Received ${message} from Parent")
    Behaviors.same
  }
}


/**
 * Exercise manage multiple child actors
 * */

object MultipleChildrenManage {

  object Parent_V2 {
    trait Command
    case class CreateChild(name: String) extends Command
    case class TellChild(name: String, message: String) extends Command

    def apply(): Behavior[Command] = active(Map())


    def active(children: Map[String, ActorRef[String]]): Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
        case CreateChild(name) =>
          context.log.info(s"[${context.self.path.name}] creating child ${name}")
          val aliceChildActorRef = context.spawn(Child(), name)
          active(children + (name -> aliceChildActorRef))

        case TellChild(name, message) =>
          context.log.info(s"[${context.self.path.name}] Sending message to child ${name}")
          children.get(name).fold(context.log.info(s"[${context.self.path.name}] ${name} not found."))(child => child ! message)
          Behaviors.same
      }
    }
  }

    def demoParentChild_V2(): Unit = {
      import Parent_V2._
      val userGuardianBehaviour: Behavior[Unit] = Behaviors.setup { context =>
        val parent = context.spawn(Parent_V2(), "Parent")
        parent ! CreateChild("Mahadev")
        parent ! CreateChild("Sahadev")
        parent ! TellChild("Mahadev", "Where are you?")
        parent ! TellChild("Jonny", "don't do that")

        Behaviors.empty
      }

      val actorSystem = ActorSystem(userGuardianBehaviour, "User")
      Thread.sleep(2000)
      actorSystem.terminate()
    }


  def main(args: Array[String]): Unit = {
    demoParentChild_V2()
  }
}


//Explore context


object ContextExample{

  object BasicActor {
    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info("Received message -->"+message)
      Behaviors.same
    }
  }

  def basicActorCall(): Unit = {
   val userGuardianBehaviour:Behavior[Unit]= Behaviors.setup{context=>
     val parentActor:ActorRef[String]= context.spawn(BasicActor(),"Parent")
     context.system.scheduler.scheduleAtFixedRate(5.seconds,5.seconds)(()=>parentActor ! "hey I am shyam")(context.executionContext)
     Behaviors.empty
   }

    val actorSystem=ActorSystem(userGuardianBehaviour,"UserGuardian")
    Thread.sleep(15000)
    actorSystem.terminate()
  }



  def main(args: Array[String]): Unit = {
    basicActorCall()
  }

}