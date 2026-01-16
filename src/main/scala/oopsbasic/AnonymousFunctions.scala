package oopsbasic

object AnonymousFunctions extends App{


  // single parameter
  val doubler=new Function1[Int,Int] {
    override def apply(v1: Int): Int = v1*2
  }


  //syntatic sugar for above method - single parameter
  val doubler_v2=(x:Int)=> x*2
  println(doubler_v2(10))


  //more sorter way - single parameter
  val doubler_v3 :Int => Int= x => x*10  // if you don't provide data type then this will not work


  /**
   *   Double parameter
   */
  val adder_v1=new Function2[Int,Int,Int] {
    override def apply(v1: Int, v2: Int): Int = v1+v2
  }

  // simple form to write above code
  val adder_v2=(a:Int,b:Int) => a+b

  //more simpler form
  val adder_v3:(Int,Int)=>Int= (a,b) => a+b

  /**
   * No - params - Interesting code below
   */
  val justDoSomeThing:()=>Int= () => 10
  println(justDoSomeThing)  // prints function itself (object address)
  println(justDoSomeThing()) // actual call



  //curly brackets
  val stringToInt = { (str:String) =>
    str.toInt
  }


  /**
   * More and more sorter syntax
   */
  val doubler_v4:Int=>Int= _ * 2
  val adder_v4:(Int,Int)=>Int = _+_



}
