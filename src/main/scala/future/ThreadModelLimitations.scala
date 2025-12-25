package future

object ThreadModelLimitations {

  class Person(var name: String)

  class BankAccount(private var amount: Int) {

    def deposit(money: Int) = this.amount += money

    def withDraw(money: Int) = this.amount -= money

    def getAmount() = amount

  }

  val bankAccount = new BankAccount(2000)
  val depositThreads = (1 to 1000).map(_ => new Thread(() => bankAccount.deposit(1)))
  val withDrawThreads = (1 to 1000).map(_ => new Thread(() => bankAccount.withDraw(1)))


  def raceCondition() = {
    (depositThreads ++ withDrawThreads).foreach(_.start())
    Thread.sleep(1000)
    println(bankAccount.getAmount())
  }

  //delegating task to running thread

  var task: Runnable = null

  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for task")
          runningThread.wait()
        }
      }


      task.synchronized {
        println("[background] I have a task")
        task.run()
        task = null
      }
    }
  })

  def delegatingToBackGroundThread(r: Runnable) = {
    if (task == null) {
      task = r
      runningThread.synchronized {
        runningThread.notify()
      }
    }
  }


  def main(args: Array[String]): Unit = {
   runningThread.start()
    delegatingToBackGroundThread(()=>println("[Task-1] I am running on different thread"))
    Thread.sleep(1000)
    delegatingToBackGroundThread(()=>println("[Task-2] I am running on again"))
    Thread.sleep(1000)
  }


}



object delegatingTask extends App{


  var task:Runnable = null

  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized{
          println("[background] Waiting for a task")
          runningThread.wait()
        }
      }

      task.synchronized{
        println("[background] I have a task")
        task.run()
        task = null
      }
    }
  })

  def delegatingTask(r:Runnable) = {
    if(task==null){
      task=r
      runningThread.synchronized{
        runningThread.notify()
      }
    }
  }


  runningThread.start()
  Thread.sleep(1000)
  delegatingTask(()=>println("[Task-1] I am running on different thread"))
  Thread.sleep(1000)
  delegatingTask(()=>println("[Task-2] I am running again"))
  Thread.sleep(1000)

}

