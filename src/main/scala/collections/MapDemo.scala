package collections

object MapDemo extends App {

  val map=scala.collection.mutable.Map[Char,Int]()

  map('a')=10

  map.foreach(println)

  map('a')=20

  map.foreach(println)

  val keyOpt=map.get('a')


}
