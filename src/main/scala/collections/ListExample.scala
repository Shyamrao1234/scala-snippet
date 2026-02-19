package collections

object ListExample extends App{

  var list = List(1,2,3,4)

  list = 0 :: list
  list = list :+ 10

  println(list)

  println(list ::: list)


}
