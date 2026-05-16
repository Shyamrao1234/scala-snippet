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
  def generalFunctor[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] = {
    functor.map(container)(_ + 1 )
  }


}
