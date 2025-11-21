package actors

import akka.actor.{Actor, ActorSystem, Props}

import scala.collection.mutable
import scala.concurrent.Future

class LoggerActor extends Actor{

  private val log = mutable.ArrayBuffer[String]()

  def receive: Receive = {
    case msg:String =>
      log.append(msg)
      println(s"Logged ${msg}")
      println("Container   "+ log)
  }

}
import scala.concurrent.ExecutionContext.Implicits.global
object MainLogger extends App{

  val actorSystem=ActorSystem("MySystem")
  val logger=actorSystem.actorOf(Props[LoggerActor])

  Future {
    for{i <- 1 to 10}{
      println(Thread.currentThread().getName)
      logger ! i.toString
    }
  }

  Future{
    for{i <- 11 to 20}{
      println(Thread.currentThread().getName)
      logger ! i.toString
    }
  }


}
