package leetcode.cli

/**
 * Created by Shyamrao on Feb 25, 2026.
 */

object Main {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      println("No argument provided")
    } else {
      println("Argument received")
      args.foreach(println)
    }
  }

}
