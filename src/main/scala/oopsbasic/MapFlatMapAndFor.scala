package oopsbasic

object MapFlatMapAndFor extends App {


  val list = List(1, 2, 3, 4)

  //map
  println(list.map(_ + 1))
  println(list.map(_ + "is a number"))

  //filter
  println(list.filter(_ % 2 == 0))

  //flatMap
  val pair: Int => List[Int] = (x: Int) => List(x, x + 1)
  println(list.flatMap(pair))


  //write code to generate all possible combination
  val list_1 = List(1, 2, 3, 4)
  val character = List('a', 'b', 'c', 'd')

  println(list_1.flatMap(n => character.map(c => "" + c + n)))


 val forRes= for {
    n <- list_1 if n%2==0
    c <- character
  } yield ""+c+n

  //this will work like foreach
  for{
    n<- list_1
  } println(n)

}
