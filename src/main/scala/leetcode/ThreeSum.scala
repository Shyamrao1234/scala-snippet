package leetcode

object ThreeSum {
  def threeSum(nums: Array[Int]): List[List[Int]] = {
    val sorted = nums.sorted
    val n = sorted.length
    var result = List[List[Int]]()

    for (i <- 0 until n - 2) {
      // Skip duplicate first elements
      if (i == 0 || sorted(i) != sorted(i - 1)) {
        var left = i + 1
        var right = n - 1

        while (left < right) {
          val sum = sorted(i) + sorted(left) + sorted(right)

          if (sum == 0) {
            // Found a valid triplet
            result = result :+ List(sorted(i), sorted(left), sorted(right))

            // Skip duplicates on both sides
            while (left < right && sorted(left) == sorted(left + 1)) left += 1
            while (left < right && sorted(right) == sorted(right - 1)) right -= 1

            // Move both pointers
            left += 1
            right -= 1

          } else if (sum < 0) {
            // Sum too small, move left pointer
            left += 1
          } else {
            // Sum too large, move right pointer
            right -= 1
          }
        }
      }
    }

    result
  }

  def main(args: Array[String]): Unit = {
    val nums = Array(-1, 0, 1, 2, -1, -4)
    val triplets = threeSum(nums)
    println(triplets.map(_.mkString("[", ", ", "]")).mkString(", "))
  }
}
