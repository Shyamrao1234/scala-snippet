package leetcode

import scala.collection.mutable

object CheckParenthesis extends App {


  def checkParenthesis(str: String) = {
    val stack = mutable.Stack[Char]()
    for (char <- str) {
      char match {
        case '(' => stack.push(char)
        case '{' => stack.push(char)
        case '[' => stack.push(char)
        case  '}' | ']' | ')' =>
      }
    }
  }

}
