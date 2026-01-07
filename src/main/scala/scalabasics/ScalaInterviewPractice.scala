package scalabasics

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ScalaInterviewPractice extends App{

  @tailrec
  def sumRec(n:Int,acc:Int):Int = {
    if(n==0) acc
    else sumRec(n-1,acc + n)
  }

//  println("tail rec:  "+sumRec(1000000,0))

  def sumLoop(n: Int): Int = {
    var acc=0
    var i=n
    while(i!=0){
      acc+=i
      i-=1
    }
    acc
  }

  //non-tail recursive
  @tailrec
  def nonTailRecSum(n:Int): Int = {
    if(n==0) n
    else nonTailRecSum(n-1)
  }

//  println("loop "+nonTailRecSum(1000000))


  case class Person(name:String,age:Int)

  val p1 = Person("shyam",10)
  val p2 = p1.copy(name="rocky") //

  println(p1)
  println(p2)

}


class Employee(name:String,age:Int)

object Run extends App{

 var count=0
  (1 to 100).par.foreach{_=>
    count+=1
  }

  println(count)



}

object CounterRaceConditionSolve extends App{

  trait CountCommand
  case object Increment extends CountCommand
  case object Get extends CountCommand

  object Counter {
    def apply()= active()

    def active(counter:Int=0):Behavior[CountCommand]=Behaviors.receive{(context,message)=>
      message match {
        case Get =>println( "count: ->"+counter)
        Behaviors.same
        case Increment => active(counter+1)
        case _ => Behaviors.same
      }

    }
  }

  val actorSystem=ActorSystem(Counter(),"Counter")

  (1 to 100000).par.foreach{_=>
    actorSystem ! Increment
  }

  Thread.sleep(1000)
  actorSystem ! Get


   val x:Nothing=throw new Exception("")
}