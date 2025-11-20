package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.omg.CosNaming.BindingHelper

private[akkatypedactors] object ActorIntro extends App {

  val simpleActorBehaviour: Behavior[String] = Behaviors.receiveMessage { message =>
    println(s"[Simple Actor] I have received message : ${message}")

    Behaviors.same
  }


  def demoSimpleActor() = {
    val actorSystem = ActorSystem(HappyPerson(), "FirstActorDemo")

    actorSystem ! "akka is bad"

    Thread.sleep(1000)
//    actorSystem.terminate()
  }

  object SimpleActor {
    def apply(): Behavior[String] = {
      Behaviors.receiveMessage { message =>

        println(s"[Simple Actor] I have received message ${message}")
        Behaviors.same
      }
    }

  }

  object SimpleActor_V1 {
    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"[Simple Actors] i have received message ${message}")
      Behaviors.same
    }
  }

  object SimpleActor_V2 {
    def apply(): Behavior[String] = Behaviors.setup { context =>


      Behaviors.receiveMessage { message =>
        context.log.info(s"[Simple Actors] i have received message ${message}")
        Behaviors.same
      }
    }
  }

  //  object PersonHappy {
  //    def apply(): Behavior[String] = Behaviors.receive { (ctx, message) =>
  //      ctx.log.info(s"I have received message ${message}, thats great")
  //      Behaviors.same
  //    }
  //  }
  //
  //  object PersonSad {
  //    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
  //      context.log.info(s"I have received a ${message}, thats sucks")
  //      Behaviors.same
  //    }
  //  }

  object Person {
    def happy(): Behavior[String] = Behaviors.receive { (ctx, message) =>
      ctx.log.info(s"I have received message ${message}, thats great")
      Behaviors.same
    }

    def sad(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"I have received a ${message}, thats sucks")
      Behaviors.same
    }
  }

  /** chnage the behaviour of an actors  */

  object HappyPerson{
    def apply():Behavior[String] = Behaviors.receive {(ctx,message)=>
      message match {
        case "akka is bad" =>
          ctx.log.info(s"received ${message} so changing behaviour to sadPerson")
          SadPerson()
        case _ =>
          ctx.log.info("Since message not matches to akka is bad so keeping behavior same")
          Behaviors.same
      }
    }
  }

  object SadPerson {
    def apply():Behavior[String]=Behaviors.receive{(ctx,mesage)=>
      mesage match {
        case "akka is awesome"=>
          ctx.log.info(s"Received : ${mesage}, so changing behaviour to happy person")
          HappyPerson()
        case _ => ctx.log.info("keeping behaviour same")
        Behaviors.same
      }
    }
  }

  demoSimpleActor()
}
