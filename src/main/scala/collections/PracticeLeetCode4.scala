package collections

object PracticeLeetCode4 extends App {

  def medianOfTwoSortedArray(nums1: Array[Int], nums2: Array[Int]): Double = {
    val (shortArray, longArray) = if (nums1.length < nums2.length) (nums1, nums2) else (nums2, nums1)

    val m = shortArray.length
    val n = longArray.length

    var low = 0
    var high = m

    while (low <= high) {
      val i = (low + high) / 2
      val j = (m + n + 1) / 2 - i

      val shortArrayLeft = if (i == 0) Int.MinValue else shortArray(i - 1)
      val shortArrayRight = if (i == m) Int.MaxValue else shortArray(i)

      val longArrayLeft = if (j == 0) Int.MinValue else longArray(j - 1)
      val longArrayRight = if (j == n) Int.MaxValue else longArray(j)

      if (shortArrayLeft <= longArrayRight && shortArrayRight >= longArrayLeft) {
        if ((m + n) % 2 == 0) {
          return (Math.max(shortArrayLeft, longArrayLeft) + Math.min(shortArrayRight, longArrayRight)) / 2.0
        } else {
          return Math.max(shortArrayLeft, longArrayLeft).toDouble
        }
      } else if (shortArrayLeft > longArrayRight) {
        high = i - 1
      } else {
        low = i + 1
      }
    }
    throw new IllegalArgumentException("Input arrays are not sorted properly.")
  }

 println( medianOfTwoSortedArray(Array(1,2,3,4),Array(5,6,7,8,9)))

}
