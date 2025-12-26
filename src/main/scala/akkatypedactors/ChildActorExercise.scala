package akkatypedactors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import org.omg.CosNaming.BindingHelper

import scala.runtime.AbstractFunction8

object ChildActorExercise {


  trait MasterProtocol

  trait WorkerProtocol

  trait UserProtocol

  //Master messages
  case class Initialize(nChildren: Int) extends MasterProtocol
  case class WordCountTask(text: String, replyTo: ActorRef[UserProtocol]) extends MasterProtocol
  case class WordCountReply(id: Int, count: Int) extends MasterProtocol

  //Worker messages
  case class WorkerTask(id: Int, text: String) extends WorkerProtocol

  //User messages
  case class Reply(count: Int) extends UserProtocol


  object WordCounterMaster {
    def apply(): Behavior[MasterProtocol] = Behaviors.receive { (context, message) =>
      message match {
        case Initialize(nChildren) =>
          context.log.info(s"[master] Initializing with ${nChildren} child")
          val childRef = for {
            i <- 1 to nChildren
          } yield context.spawn(WordCounterWorker(context.self), s"child-$i")
          active(childRef,0,0,Map())
        case _=>
          context.log.info("[master] Command not supported")
          Behaviors.same
      }
    }


    def active(childRefs: Seq[ActorRef[WorkerProtocol]],
               currentChildIndex: Int,
               currentTaskId:Int,
               requestMap:Map[Int,ActorRef[UserProtocol]]
              ): Behavior[MasterProtocol] =
      Behaviors.receive { (context, message) =>
      message match {
        case WordCountTask(text,replyTo)=>
          context.log.info(s"[master] I've received a ${text} and I will send it to child $currentChildIndex")
          val task=WorkerTask(currentTaskId,text)
          val childRef= childRefs(currentChildIndex)
          childRef ! task
          //update the data
          val nextChildIndex= currentChildIndex + 1 % childRefs.length
          val nextTaskId=currentTaskId+1
          val newRequestMap= requestMap + (currentChildIndex -> replyTo)
          active(childRefs,nextChildIndex,nextTaskId,newRequestMap)
        case WordCountReply(id,count)=>
          context.log.info(s"[maste] I've received a reply for task id ${id} with ${count}")
          val originalSender= requestMap(id)
          originalSender ! Reply(count)
          active(childRefs,currentChildIndex, currentTaskId, requestMap-1)

        case _ =>
          context.log.info("[master] Command not supported")
          Behaviors.same
      }
    }
  }

  object WordCounterWorker {
    def apply(masterRef:ActorRef[MasterProtocol]): Behavior[WorkerProtocol] =Behaviors.receive {(context,message)=>
      message match {
        case WorkerTask(id,text)=>
          context.log.info(s"[worker] I've received task ${id} with $text")
          val result=text.split(" ").length
          masterRef ! WordCountReply(id,result)
         Behaviors.same
        case _=>
          Behaviors.same
      }
    }
  }


  object Aggregator {
    def apply(): Behavior[UserProtocol] = active()


    def active(totalCount: Int = 0): Behavior[UserProtocol] = Behaviors.receive { (context, message) =>
      message match {
        case Reply(count) =>
          context.log.info(s"[aggregator] I've received a $count and total count : $totalCount")
          active(totalCount + count)
      }
    }
  }


  def testWordCounter(): Unit = {
    val userGuardianBehaviour: Behavior[Unit] = Behaviors.setup { context =>
      val aggregatorActor = context.spawn(Aggregator(), "Aggregator")
      val wcm = context.spawn(WordCounterMaster(), "WordCounterMaster")

      wcm ! Initialize(3)
      wcm ! WordCountTask("Hello I am shyam", aggregatorActor)
      wcm ! WordCountTask("Ram is innocent", aggregatorActor)
      wcm ! WordCountTask("yes, Akka is super", aggregatorActor)

      Behaviors.empty
    }

    val wordCounterActor = ActorSystem(userGuardianBehaviour, "WordCountingActor")
    Thread.sleep(1000)
    wordCounterActor.terminate()
  }


  def main(args: Array[String]): Unit = {
    testWordCounter()
  }
}