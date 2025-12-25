package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object ManagingActorState {


  /*Exercise : Use setup method to create word counter which
  -splits each message into words
  -keeps message of total number of words received so far
  -logs current number of words and total number of words
  */

  object WordCounterActor{
    def apply():Behavior[String]=Behaviors.setup{context=>
      var total=0
      Behaviors.receiveMessage{message=>
        val newCount=message.split(" ").length
        total += newCount
        context.log.info(s"Number of word in a sentence is ${newCount} and total count is ${total}")
        Behaviors.same
      }
    }
  }

  def wordCounterCall() = {
    val wordCounterActor=ActorSystem(WordCounterActor(),"WordCaller")
    wordCounterActor ! "hello I am shyam"
    wordCounterActor ! "I love akka actors"
    Thread.sleep(1000)
    wordCounterActor.terminate()
  }

  trait SimpleThings
  case object EatingFood extends SimpleThings
  case object WashingFloor extends SimpleThings
  case object LearningAkka extends SimpleThings


  object SimpleHuman{
    def apply():Behavior[SimpleThings]=Behaviors.setup {context=>
      var happiness=0

      Behaviors.receiveMessage{message=>
        message match {
          case EatingFood =>
            happiness += 1
            context.log.info(s"Eating Food, Happiness count increased to : ${happiness}")
            Behaviors.same
          case LearningAkka =>
            happiness += 99
            context.log.info(s"Learning Akka, Happiness count increased to : ${happiness}")
            Behaviors.same
          case WashingFloor =>
            happiness -= 1
            context.log.info(s"Washing Floor, Happiness count decreased to: ${happiness}")
            Behaviors.same
        }
      }
    }
  }

  def simpleHumanCall(): Unit = {
    val human=ActorSystem(SimpleHuman(),"SimpleThings")
    human ! EatingFood
    human ! LearningAkka
    (1 to 30).foreach(each=> human ! WashingFloor)
  }



  object SimpleHuman_V2{

    def apply() = {
      stateLessHuman(0)
    }

    def stateLessHuman(happiness: Int):Behavior[SimpleThings]=Behaviors.receive{(context,messsage)=>
      messsage match {
        case EatingFood =>
          context.log.info(s"Eating Food, Happiness Count: ${happiness}")
          stateLessHuman(happiness + 1)
        case LearningAkka =>
          context.log.info(s"Learning Akka, Happiness Count: ${happiness}")
          stateLessHuman(happiness+99)
        case WashingFloor => context.log.info(s"Washing floor : ${happiness}")
        stateLessHuman(happiness  -1)
      }
    }
  }

  def simpleHumanCall_V2() = {
    val simpleHuman=ActorSystem(SimpleHuman_V2(),"SimpleHuman_V2")
    simpleHuman ! EatingFood
    simpleHuman ! LearningAkka
    (1 to 30).foreach{each=> simpleHuman ! WashingFloor}

    Thread.sleep(1000)
    simpleHuman.terminate()
  }




  def main(args: Array[String]): Unit = {
    simpleHumanCall_V2()
  }

}
