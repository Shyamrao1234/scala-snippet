package scalabasics

object PatternEveryWhere extends App{


  val list=List(1,2,3,4,5)

  val forList = for{
    x <- list if(x %2 ==0)
  } yield x

  val listTuple=List((1,2),(3,4))
  val tupleYield = for{
    (first,second) <- listTuple
  }yield  first+second


  val head :: tail = list.filter(_ == 10)
  println(head)
}
