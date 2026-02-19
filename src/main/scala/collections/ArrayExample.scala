package collections

object ArrayExample extends App {

  val array1 = new Array[Char](10)

  array1.foreach(println)

  val tabulatedArray = Array.tabulate(5)(i=> i).foreach(println)





}
