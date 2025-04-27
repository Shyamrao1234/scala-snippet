package actors

import akka.actor.{Actor, ActorSystem, Props}

object ExampleOne extends App{

    class SimpleActor extends Actor {
      override def receive: Receive = {
        case s: String => println(s"string ${s}")
        case i: Int => println(s"int ${i}")
      }
    }

    val system = ActorSystem("SimpleSystem")
    val actor = system.actorOf(Props[SimpleActor])

    actor ! "hii this is shyam"
    actor ! 25

  system.terminate()
  }

