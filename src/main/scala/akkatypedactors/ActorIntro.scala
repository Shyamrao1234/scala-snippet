package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akkatypedactors.ActorIntroV1.BetterActor.NumberMessage
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

  /** chnage the behaviour of an actors */

  object HappyPerson {
    def apply(): Behavior[String] = Behaviors.receive { (ctx, message) =>
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
    def apply(): Behavior[String] = Behaviors.receive { (ctx, mesage) =>
      mesage match {
        case "akka is awesome" =>
          ctx.log.info(s"Received : ${mesage}, so changing behaviour to happy person")
          HappyPerson()
        case _ => ctx.log.info("keeping behaviour same")
          Behaviors.same
      }
    }
  }

  demoSimpleActor()
}


object ActorIntroV1 {

  val simpleActor: Behavior[String] = Behaviors.receiveMessage { message =>
    println(s"I have received message ${message}")
    Behaviors.same
  }


  //generally there are three ways that  we can create Behaviours
  object SimpleActor_v1 {
    def apply(): Behavior[String] = Behaviors.receiveMessage { message =>
      println(s"I have received a message ${message}")
      Behaviors.same
    }
  }

  object SimpleActor_V2 {
    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.info(s"I have received a message: ${message}")
      Behaviors.same
    }
  }

  object SimpleActor_V3 {
    def apply(): Behavior[String] = Behaviors.setup { context =>
      //YOUR CODE HERE
      Behaviors.receiveMessage { message =>
        context.log.info(s"I have received a message ${message}")
        Behaviors.same
      }
    }
  }




  //Actor changing the behaviour

  object Person {

    def happy(): Behavior[String] = Behaviors.receive { (context, message) =>
      message match {
        case "Akka is bad" =>
          context.log.info(s"Don't talk about akka like this, I am sad")
          sad()

        case _ => context.log.info("I have received a message, That's great")
          Behaviors.same
      }
    }


    def sad(): Behavior[String] = Behaviors.receive { (context, message) =>
      message match {
        case "Akka is good" =>
          context.log.info(s"Yeah akka is good, I am happy now")
          happy()

        case _ => context.log.info(s"I have received a message ${message}, That's suck")
          Behaviors.same
      }
    }
  }

  def personActorCall() = {
    val actorSystem = ActorSystem(Person.happy(), "FirstActor")
    actorSystem ! "Akka is bad"
    actorSystem ! "I am shyam"
    actorSystem ! "Akka is good"
    actorSystem ! "I am your dad"
    actorSystem ! "akka is my love"
  }


  object WeirdActor {
    def apply(): Behavior[Any] = Behaviors.receive { case (context, message) =>
      message match {
        case str: String =>
          context.log.info(s"I have received a string message ${str}")
          Behaviors.same
        case number: Int =>
          context.log.info(s"I have received a int message ${number}")
          Behaviors.same
      }
    }
  }

  //this will fail this there is no typeSafety
  def weirdActorCall() = {
    val weirdActor = ActorSystem(WeirdActor(), "WeirdActor")
    weirdActor ! 45
    weirdActor ! "Actor"
    weirdActor ! '\t'
  }


  // this is for typeSafety
  object BetterActor {
    trait Message

    case class StringMessage(str: String) extends Message

    case class NumberMessage(number: Int) extends Message

    def apply(): Behavior[Message] = Behaviors.receive { case (context, message) =>
      message match {
        case str: StringMessage => context.log.info("I have received a string")
          Behaviors.same

        case num: NumberMessage => context.log.info("I have received a number")
          Behaviors.same
      }
    }
  }

  def betterActorCall() = {
    import BetterActor._
    val betterActor = ActorSystem(BetterActor(), "WeirdActor")
    betterActor ! NumberMessage(45)
    betterActor ! StringMessage("Actor")
  }


  def main(args: Array[String]): Unit = {
    val increment:Int=>Int = x => x+1
    val list=List(1,2,3,4,5)
    list.map(increment).foreach(println) //ETA-Expansion
  }

}


