package leetcode.pratyushcoding

/**
 * Created by Shyamrao on Jun 25, 2026.
 */

object Day3 extends App {


  /**
   *
   * Given an sorted array and find square and add in new array in sorted
   *
   */

  private def bruteForceApproach(array: Array[Int]) = {
    val n        = array.length
    var temp     = 0
    val newArray = new Array[Int](n)
    for (i <- 0 until n) {
      newArray(i) = array(i) * array(i)
    }

    for (i <- 0 until n - 1) {
      for (j <- i + 1 until n) {
        if (newArray(i) > newArray(j)) {
          temp = newArray(i)
          newArray(i) = newArray(j)
          newArray(j) = temp
        }
      }
    }
    newArray
  }

  bruteForceApproach(Array(-4, -1, 0, 3, 10)).toList.foreach(print)


  

}
