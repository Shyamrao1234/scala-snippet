package collections

import scala.collection.mutable

object StackExample extends App{


  val stack = mutable.Stack[Int]()

  stack.push(10)
  stack.push(20)
  stack.push(30)

  println(stack)

  println(stack.pop())

}
