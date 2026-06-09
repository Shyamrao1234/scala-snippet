package catstopic

import scala.language.higherKinds
import scala.util.Try


object FunctorExample extends App {

  //Functor definition
  trait MyFunctor[F[_]] {
    def map[A, B](inialValue: F[A])(f: A => B)
  }

  //Functor is for list,option and try

  import cats.Functor
  import cats.instances.list._
  import cats.instances.option._
  import cats.instances.try_._
  import cats.instances.int._

  val listFunctor = Functor[List].map(List(1, 2, 3))(_ + 1)
  val optionFunctor = Functor[Option].map(Option(2))(_ + 2)
  val tryFunctor = Functor[Try].map(Try(22))(_ + 2)


  /**
   * We can create generalized method
   */

  def generalFunctor[F[_]](container: F[Int])(implicit fn: Functor[F]) = {
    fn.map(container)(_ + 1)
  }

  trait Tree[+T]

  //smart constructor
  object Tree {
    def branch[T](value: T, left: Tree[T], right: Tree[T]): Tree[T] = Branch(value, left, right)
    def leaf[T](value: T): Tree[T] = Leaf(value)

  }

  case class Leaf[+T](value: T) extends Tree[T]

  case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

  implicit object treeFunctor extends Functor[Tree] {

    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(value) => Leaf(f(value))
      case Branch(value, left, right) => Branch(f(value), map(left)(f), map(right)(f))
    }
  }

  import cats.syntax.functor._

  val tree: Tree[Int] = Tree.branch(10,Tree.branch(20,Tree.leaf(30),Tree.leaf(40)),Tree.leaf(50))

  println("Tree data**** "+ tree.map(_ * 10))


  println(generalFunctor(List(1, 2, 3)))
  println(generalFunctor(Option(1)))
  println(generalFunctor(Try(10)))


}


