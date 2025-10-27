package scalabasics

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.Random
//
//
///**
// * Traits are like java interface, but more powerful than interface
// * we can create abstract and concrete methods in traits
// */
//class TraitsConcept {
//
//}
//
//
//trait Test1 {
//
//  def test1(): Unit = {
//    println("test1")
//  }
//}
//
//trait Test2 {
//
//  def test1(): Unit = {
//    println("test2")
//  }
//}
//
//class Main extends Test1 with Test2 {
//  println(test1())
//}

import scala.concurrent.ExecutionContext.Implicits.global

object FutureExample extends App {

  var tractStatus = ""

  def runFuture(list: List[String]) = {
    for {
      _ <- Future.unit
      _ = list.map { x =>
        Thread.sleep(Random.nextInt(5))
        if (tractStatus != x) {
          tractStatus = x
          println("status :  " + tractStatus)
          println("ThreadName :  " + Thread.currentThread().getName)
        }
      }
    } yield ""
  }

  val states = Array("on", "off")

  val list = (1 to 1000).map(_ => states(Random.nextInt(states.length))).toList

  Await.result(runFuture(list),5.seconds)


}