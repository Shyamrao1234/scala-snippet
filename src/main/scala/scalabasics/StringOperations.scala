package scalabasics


object StringOperations extends App {


  //  val x : String   = "shyam"
  //  val a : String   = "a"
  //  val re: Seq[Any] = a :+ x
  //  println(re)
  //
  //
  //  //F-interpolate
  //  val pi = 3.1245
  //  println(f"pi : $pi%.2f")
  //
  //  val d = "abc"
  //  println(f"$d%s")


  def firstNonRepeatingChar(s: String) = {
    //    val linkedHashMap = scala.collection.mutable.LinkedHashMap[Char, Int]()
    //
    //    for (c <- s) {
    //      linkedHashMap(c) = linkedHashMap.getOrElse(c, 0) + 1
    //    }
    //    linkedHashMap.minBy(_._2)._1

    val freq = new Array[Char](10)


    freq.foreach(println)
  }


  firstNonRepeatingChar("aabbcd")

}
