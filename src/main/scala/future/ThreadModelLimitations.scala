package future

object ThreadModelLimitations{

  class Person(var name:String)

  class BankAccount(private var amount:Int){

    def deposit(money:Int)= this.amount += money

    def withDraw(money:Int)=this.amount -= money

    def getAmount()= amount

  }

  val bankAccount=new BankAccount(2000)
  val depositThreads=(1 to 1000).map(_=>new Thread(()=>bankAccount.deposit(1)))
  val withDrawThreads= (1 to 1000).map(_=> new Thread(()=>bankAccount.withDraw(1)))

  def main(args: Array[String]): Unit = {
    (depositThreads ++ withDrawThreads).foreach(_.start())
     Thread.sleep(1000)
    println(bankAccount.getAmount())


  }


}


