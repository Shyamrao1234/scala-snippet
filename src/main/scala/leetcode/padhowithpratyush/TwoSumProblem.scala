package leetcode.padhowithpratyush

object TwoSumProblem extends App {

  def executeWithBF(arrays: Array[Int], target: Int) = {
    var result = (-1, -1)
    for {
      i <- arrays.indices
      j <- i + 1 until arrays.length
      sum = arrays(i) + arrays(j)
      _ = if (sum == target) {
        result = (arrays(i), arrays(j))
      }
    } yield result
    result
  }

  println(executeWithBF(Array(2, 7, 11, 15), 9))


  def executeScalaFunctional(arrays: Array[Int], target: Int) = {
    arrays.indices.iterator.map { i =>
      (i + 1 until arrays.length).iterator.collectFirst {
        case j if arrays(i) + arrays(j) == target => (arrays(i), arrays(j))
      }
    }.toList.head
  }

  println(executeScalaFunctional(Array(2, 12, 11, 15), 9))


  // assuming arrays will come in sorted form
  def executeWithTwoPointer(arrays: Array[Int], target: Int): (Int, Int) = {
    var left = 0
    var right = arrays.length - 1
    while (left < right) {
      val sum = arrays(left) + arrays(right)
      if (sum == target) {
        return (arrays(left), arrays(right))
      } else if (sum < target) {
        left += 1
      } else right -= 1
    }
     (-1,-1)
  }

  println(executeWithTwoPointer(Array(2, 7, 11, 15), 9))

}
