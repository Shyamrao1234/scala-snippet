package scalabasics

/**
 * Created by LENOVO on Apr 17, 2026.
 */

object ArraysPratice extends App {


  val array: Seq[String] = Array("Krishna")

  print(array)


}

object FindMax extends App {


  def max(array: Array[Int]) = {
    var max = 0
    for {i <- array.indices} {
      max = Math.max(max, array(i))
    }
    max
  }


  println(max(Array(1, 2, 3, 14, 4)))


}

object SecondMax extends App {

  def secondMax(array: Array[Int]) = {

    var max = 0
    var secMax = 0
    for (i <- array.indices) {
      secMax = max
      max = math.max(max, array(i))
    }
    secMax
  }


  def merge(a: Array[Int], b: Array[Int]): Array[Int] = {
    val res = new Array[Int](a.length + b.length)
    var i, j, k = 0

    while (i < a.length && j < b.length) {
      if (a(i) < b(j)) { res(k) = a(i); i += 1 }
      else { res(k) = b(j); j += 1 }
      k += 1
    }

    while (i < a.length) { res(k) = a(i); i += 1; k += 1 }
    while (j < b.length) { res(k) = b(j); j += 1; k += 1 }

    res
  }

  merge(Array(1,4,5,23,21),Array(10,23,45,56)).foreach(println)



}

