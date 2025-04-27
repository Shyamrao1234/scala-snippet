package collections

import java.util

case class Student(name: String, age: Int, rollNo: Int, percentage: Double)

object SeqDemo extends App {

  val seq = Seq(Student("A", 20, 1, 80.0), Student("B", 21, 2, 70.2), Student("C", 22, 3, 35.8))

  val array = Array(1, 2, 3)

  //toArray method will just wrap into array
  seq.toArray.getClass

  //create new new array and copy the seq data into new array
//  Array.empty[Int].iterableFactory.from(seq)


  //returns the combinations
  seq.combinations(2).foreach(println)

  /**
   * Instead of usinf filter and map you can use collect method
   */
  val handleA: PartialFunction[Student, Student] = {
    case x if (x.name == "A") => x
  }
  seq.collect(handleA).foreach(println)


  /**
   * Instead of doing this circus use below code (collect1.3) :- Seq("A", "2", 6, 6.0).filter(_.isInstanceOf[String]).map(_.asInstanceOf[String])
   */
  println("collect 1.3------>" + Seq("A", "2", 6, 6.0).collect { case x: String => x })


  val f1: Int => Int = x => x + 1
  val f2: Int => Int = x => x * 2
  /**
   * It will work f1(f2(x))
   */
  val composed = f1.compose(f2)
  println("compose 1.0 --> " + composed(2))


  /**
   * if all the elements of sliceSeq is present in Seq1 then this return true
   */
  val seq1 = Seq(1, 2, 3, 4)
  val sliceSeq = Seq(2, 3, 4)
  println("containsSlice 1.0---->" + seq1.containsSlice(sliceSeq))


  val gropedSeq = Seq(1, 2, 3, 4).grouped(3).toSeq
  println("Groped 1.0--->" + gropedSeq)


  //  val grouped=Seq(1,2,3,4).groupBy()



  println("apply--->" + Seq(1, 2, 3).apply(1))

  println("isDefinedAt--->" + Seq(10, 20, 30).isDefinedAt(4))

  println("indices--->" + Seq(1, 2, 3).indices)


  val indeces = Seq(10, 20, 30)
  for (i <- indeces.indices) {
    println(indeces(i))
  }


//  println(Seq(20,30,50).lengthCompare(Seq(10,20,30,10,40)))

  





}
