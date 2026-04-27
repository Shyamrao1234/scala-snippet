package leetcode

import scala.reflect.ClassTag

object BinarySearch extends App {


  /** *
   * Keep in mind :  list should be sorted
   */

  def search(list: List[Int], target: Int): Int = {
    var low  = 0
    var high = list.length - 1
    while (low < high) {
      var mid = low + (high - low) / 2
      if (list(mid) == target) return mid
      else if (list(mid) > target) high = mid - 1
      else low = mid + 1
    }
    -1
  }


  println(search(List(1, 2, 3, 4, 5, 6, 7, 8), 4))

}


object Deummyyee extends App {

  private def execute[T:ClassTag](size: Int): Array[T] = {
    new Array[T](size)
  }

  execute(10)


}
