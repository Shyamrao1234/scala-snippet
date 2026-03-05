package oopsbasic

import org.joda.time.DateTime


case class Computer(name: String, core: Int)

object Computer {
  implicit val ordering: Ordering[Computer] = Ordering.by((c: Computer) => (c.name, c.core))
}

object OrderingExamples extends App {


//  val list = List(4, 32, 5, 0, 65)
//  println(list.sorted(Ordering[Int].reverse))
//
//  val listOfComputer = List(Computer("hp", 2), Computer("mac", 10), Computer("Asus", 4), Computer("Asus", 1))
//  println(listOfComputer.sorted)


  case class Employee(name: String, dateTime: DateTime)
  val listOfDataTime = List(
    Employee("name",DateTime.now())
  )


}
