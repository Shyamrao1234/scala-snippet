package generics

object Example1 extends App{


//   class Stack[A] {
//     private var elements:List[A] = Nil
//
//     def push(x:A) = elements = x :: elements
//
//
//   }

  val x=Option(10).map(x=>Some(x))

  val y:Byte= -12
  val t:Short= 12312

//  val j = ???

//  val ex= if(false) throw new RuntimeException("") else 10
//  val _1 = if(false) 10 else 10

  case class Person(name:String)
  val p1=Person("shyam")
  val p2=Person("shyam")
  println(" == : "+(p1==p2))

  val str1=null
  val str2=new String("Hello")
  println(str2==str1)


//  val list=List(1,2,3)
//  val updatedList=  list :+ 1
//  updatedList.foreach(println)


}
