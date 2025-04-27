package actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.sys.Prop

object ActorCountDown extends App {
  case class StartCounting(n: Int, actor: ActorRef)
  case class CountDown(n: Int)

  class CountDownActor extends Actor {

    override def receive: Receive = {
      case StartCounting(n,other) =>
        println(n)
        println(s"$n from ${self.path.name}")
        other ! CountDown(n-1)
      case CountDown(n)=>
        if(n>0){
          println(n)
          println(s"$n from ${self.path.name}")
          sender ! CountDown(n-1)
        }else{
          println(s"$n from ${self.path.name}")
          context.system.terminate()
        }
    }
  }

  val system = ActorSystem("simplesystem")
  val actor1 = system.actorOf(Props[CountDownActor], "CountDown1")
  val actor2 = system.actorOf(Props[CountDownActor], "CountDown2")

  actor1 ! StartCounting(10, actor2)

}
