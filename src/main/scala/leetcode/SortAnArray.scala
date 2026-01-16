package leetcode

object SortAnArray extends App {


  val array=Array(8,6,1,5,3,4,3)

  val n=array.length
  for(i<-0 until n-1) {
    for(j <- i+1 to n){
      if(array(i)>array(j)){
        var temp=array(j)
        array(j) = array(i)
        array(i) = temp
      }
    }
  }
}
