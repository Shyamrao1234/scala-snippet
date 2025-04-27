import scala.reflect.ClassTag

object Main {
  def main(args: Array[String]): Unit = {


    //    def printFirst[T](list:List[T])=list.head

    //    def isString[T](list:List[T])=list.isInstanceOf[List[String]]
    //
    //    println(isString(List("hello")))
    //
    //    def makeArray[T](size: Int): Array[T] = new Array[T](size) // ERROR
    //
    //    makeArray(10)

    def makeArrays[T: ClassTag](size: Int) = new Array[T](size)

    println(makeArrays(10))

    def makeArray[T](size: Int)(implicit ct: ClassTag[T]): Array[T] = ct.newArray(size)

    def demo(list: Seq[String]): Unit = {
      println()
    }

    //    val array=new Array[String](2)("A","B")

    val list = Array(1, 2, 3, 4)

    val newList1 = list :+ 5

    val prespendList= 0 +: newList1

    prespendList.foreach(println)

  }
}