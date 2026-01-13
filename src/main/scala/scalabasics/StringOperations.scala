package scalabasics


object StringOperations extends App{


  val x:String = "shyam"
  val a:String="a"
  val re: Seq[Any] = a :+ x
  println(re)


  //F-interpolate
  val pi=3.1245
  println(f"pi : $pi%.2f")

  val d="abc"
  println(f"$d%s")

}
