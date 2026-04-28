package threads

/**
 * Created by Shyamrao on Apr 14, 2026.
 */

object OddEvenPrinter extends App{

  val MAX     = 10
  var current = 1
  val lock    = new AnyRef

  val oddThread = new Thread(() => {
    lock.synchronized {
      while (current < MAX) {
        if (current % 2 == 0) {
          lock.wait()
        } else {
          println(s"Odd Thread : $current")
          current += 1
          lock.notify()
        }
      }
    }
  })


  val evenThread = new Thread(() => {
    lock.synchronized {
      while (current < MAX) {
        if (current % 2 != 0) {
          lock.wait()
        } else {
          println(s"Even Number : $current")
          current += 1
          lock.notify()
        }
      }
    }
  })


  //start both the threads
  evenThread.start()
  oddThread.start()

  //wait for both threads to finish before existing main
  evenThread.join()
  oddThread.join()


}
