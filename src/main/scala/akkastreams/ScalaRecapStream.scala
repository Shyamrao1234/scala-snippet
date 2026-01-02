package akkastreams

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ScalaRecapStream extends App {

  def testFuture() = {
    val future: Future[Int] = Future {
      10 / 0
    }
    val recover:Future[Int]= future.recoverWith { case _: Exception => Future(0) }
    recover.onComplete {
      case Failure(exception) => println("failed with exception "+exception)
      case Success(value) => println("Success with value :"+value)
    }
    Thread.sleep(1000)
  }


  def typeAlias(): Unit = {
    type akkaReceiver= PartialFunction[Any,Unit]

    val funtion:akkaReceiver={
      case 1=>println("first")
      case 2=> println("confused")
    }
    funtion(1)
  }

  typeAlias()


}


object ImplicitExample extends App{

//  case class Person(name:String) {
//    def toPerson:String="Hii I am Rocky"
//  }
//
//  implicit  def fromStringToPerson(name:String)=Person(name)
//
//   "hello".toPerson
//  //fromStringToPerson("hello").toPerson


  implicit val numberOrdering:Ordering[Int]=Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted.foreach(println)

}


