package billionrowchallenge

import java.io.{BufferedWriter, File, FileOutputStream, FileWriter, OutputStreamWriter}
import java.util.concurrent.Executors
import scala.util.Random

/**
 * Created by Shyamrao on Mar 09, 2026.
 */

object Write1BRFile extends App {


  val cities = Array(
    "Amsterdam", "Berlin", "Chennai", "Delhi", "London", "Mumbai",
    "NewYork", "Paris", "Sydney", "Tokyo", "Dubai", "Rome", "Madrid"
  )

  val rows    = 1000000000
  val threads = Runtime.getRuntime.availableProcessors()


  val rows_PerThread = rows / threads
  val executor       = Executors.newFixedThreadPool(threads)
  val file           = new File("measurements.txt")
  val fos            = new FileOutputStream(file)
  val writer         = new BufferedWriter(new OutputStreamWriter(fos), 10 * 1024 * 1024)

  for (t <- 1 to threads) {
    handleRow(writer)
  }

  executor.shutdown()
  while (!executor.isTerminated) {}
  writer.close()
  println("Finished generation 1B row")

  def handleRow(writer: BufferedWriter) = {
    new Runnable {
      override def run(): Unit = {
        val random = new Random()
        val sb     = new StringBuilder(1024 * 1024)

        for (i <- 1 to rows_PerThread) {
          val city = cities(random.nextInt(cities.length))
          val temp = Math.round((random.nextDouble() * 50 - 10) * 10) / 10.0
          sb.append(city)
            .append(";")
            .append(temp)
            .append("\n")

          if (sb.length > 1000000) {
            writer.synchronized {
              writer.write(sb.toString())
            }
            sb.clear()
          }
        }

      }
    }
  }


}


object OccurenceOFEach extends App {

  val str = "scala"

  str.groupBy(identity).collect { case (k, v) =>
    (k, v.length)
  }.foreach(println)

}