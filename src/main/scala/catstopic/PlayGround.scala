package catstopic

import cats.Eval

/**
 * Created by Shyamrao on Apr 06, 2026.
 */

object PlayGround extends App{

  val meaningOfLife = Eval.later{
    println("Learning cats")
    42
  }

  println(meaningOfLife.value)

}
