package collections

object VectorExample extends App {


  val vec = Vector(1, 2, 3, 4)

  println(vec(0))
  println(vec :+ 10) //append
  println(0 +: vec) //prepend
  println(vec.updated(2, 7)) //update


}
