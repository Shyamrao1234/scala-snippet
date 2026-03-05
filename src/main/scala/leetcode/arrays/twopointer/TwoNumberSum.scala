package leetcode.arrays.twopointer

/**
 * Created by Shyamrao on Feb 21, 2026.
 */


/**
 * using two pointer
 *
 */

object TwoNumberSum extends App{


  def twoPointers(array: Array[Int], target: Int) = {


    def innerMethod(left: Int, right: Int):(Int, Int) = {
      if(left==right){
        throw new RuntimeException("No value found")
      } else if (array(left) + array(right) == target) (left, right)
      else if (array(left) + array(right) < target) innerMethod(left+1,right)
      else innerMethod(left,right-1)
    }
   innerMethod(0,array.length-1)
  }

  println(twoPointers(Array(2,3,5,8),9))


}
