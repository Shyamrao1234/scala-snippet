package part1recap

object ScalaRecap extends App{

  //expression
//  val ifElseExp=if(2>3)"bigger" else "smaller"
//
//  val whileExp=while(true) {} // is not a expression
//
//
//  val operators=1.+(2)



  val list=List(1,2,3,4,5)

  //FP
  def increment:Int => Int=x=>x+1

//  def increment(x:Int)=x+1

  val reAssign=increment(10)

  println(reAssign)


  def makeAdder(x:Int):Int => Int = y => x+y
  val added5=makeAdder(5)

  println(added5(10))

  val checkerBoard=List(1,2,3).flatMap(n=>List("a","b","c").map(c=>(n,c)))
  val checkerBoard2= for{
    n<-List(1,2,3,4)
    c<- List("a","b","c")
  }yield (n,c)



  //implicits
  implicit val timeOut=3000
  def setTimeOut(f:()=>Unit)(implicit tout:Int): Unit = {
    Thread.sleep(1000)
    f
  }

  setTimeOut(()=>println("thread is sleeping"))



  implicit class MyRichInt(x:Int){

    def isEven={
      println("Custom even implementation")
      if(x%2==0) true else false
    }

  }

  println(10.isEven)

}
