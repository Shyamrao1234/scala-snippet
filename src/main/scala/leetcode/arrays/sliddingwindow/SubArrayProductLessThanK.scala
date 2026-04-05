package leetcode.arrays.sliddingwindow

/**
 * Created by Shyamrao on Mar 11, 2026.
 */


/** *
 * Given an array of integers nums and an integer k,
 * return the number of contiguous subarrays
 * where the product of all the elements in the subarray is strictly less than k.
 */

object SubArrayProductLessThanK extends App {

  /**
   * Using brute force
   */
  def execute(array: Array[Int],
              k: Int
             ) = {
    var count = 0
    for (i <- 0 until array.length) {
      var product = 1
      for (j <- i until array.length) {
        product *= array(j)
        if (product < k) {
          count += 1
        } else {
          j == array.length
        }
      }
    }
    count
  }


  def executeInternal(array: Array[Int],
                      k: Int
                     ) = {
    var left    = 0
    var product = 1
    var count   = 0
    for (r <- 0 until array.length) {
      product *= array(r)
      while (product >= k && left <= r) {
        product = product / array(left)
        left += 1
      }
      count += r - left + 1
    }
    count
  }

  println(executeInternal(Array(10, 5, 2, 6), 100))

}


object SonyInterviewProgram extends App {

  /** *
   * /**
   * * val logs = List(
   * "user1:sony tv",
   * "user2:sony camera",
   * "user1:sony headphones",
   * "user3:canon camera",
   * "user2:sony tv"
   * )
   * extract the userId and search term  group by user
   * for each user count the total searches made
   * return the highest occurence of a key in the search made by each user.
   * *
   * *
   * *
   * *
   * */
   */

  case class Components(userId: String, searchTerms: String)

  val extractUser: String => Components = x => {
    val splitRes    = x.split(":")
    val userId      = splitRes.head
    val searchTerms = splitRes.last
    Components(userId, searchTerms)
  }

  val logs = List(
    "user1:sony tv",
    "user2:sony camera",
    "user1:sony headphones",
    "user3:canon camera",
    "user2:sony tv"
  )

  logs.map(extractUser).groupBy(_.userId).map { case (userId, searchTerms) =>

    val maxSearch = searchTerms.flatMap(_.searchTerms.split(" "))
      .groupBy(identity)
      .map { case (k, v) => (k, v.length) }
      .maxBy(_._2)

    (userId,maxSearch._1,maxSearch._2)
  }.foreach(println)


}














