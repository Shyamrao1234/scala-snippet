package leetcode

object LCS extends App {

 val list=scala.collection.mutable.ListBuffer.empty[Char]

 def lcs(str1:String,str2:String) = {

  def f(s1:Int,s2:Int):Int = {
   if(s1<0 || s2<0)  0
   else if(str1(s1)==str2(s2)){
    list.append(str1(s1))
    1+ f(s1-1,s2-1)
   } else {
    val left=f(s1-1,s2)
    val right=f(s1,s2-1)
    if(left>=right) left else right
   }
  }
  f(str1.length-1,str2.length-1)
 }

 println(lcs("AGGTAB","GXTXAYB"))

  list.foreach(print)



}

object Demo extends App{
 def lcs(str1: String, str2: String): (Int, String) = {

  def f(s1: Int, s2: Int): (Int, String) = {
   if (s1 < 0 || s2 < 0) (0, "")
   else if (str1(s1) == str2(s2)) {
    val (len, seq) = f(s1 - 1, s2 - 1)
    (len + 1, seq + str1(s1)) // Append character in correct order
   } else {
    val left = f(s1 - 1, s2)
    val right = f(s1, s2 - 1)
    if (left._1 >= right._1) left else right
   }
  }

  f(str1.length - 1, str2.length - 1)
 }

 println(lcs("caba","abac"))


}
