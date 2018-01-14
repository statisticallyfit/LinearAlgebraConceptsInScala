package org.fp.studies.functor.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.AutoExamples

/**
  *
  * @see [[operator_map]]
  *
  */
package object withcustommap {

  /**
    * @see [[ann_FunctionComposition]]
    */
  trait AmountExample {

    sealed trait Amount[A]
    case class One[A](a: A) extends Amount[A]
    case class Couple[A](a: A, b: A) extends Amount[A]
    case class Few[A](a: A, b: A, c: A) extends Amount[A]
  }

  /**
    * @see [[Scalaz]]
    */
  object AmountExample_FunctorScalaz extends AmountExample {

    import scalaz.Functor

    implicit val functor: Functor[Amount] =
      new Functor[Amount] {
        def map[A, B](fa: Amount[A])(f: A => B): Amount[B] =
          fa match {
            case One(a) => One(f(a))
            case Couple(a, b) => Couple(f(a), f(b))
            case Few(a, b, c) => Few(f(a), f(b), f(c))
          }
      }

    //todo
//    private val one: Functor[Amount[Int]] = One(6)
//    private val intToInt: (Int) => Int = { x: Int => x * 7 }
//    one map intToInt
  }

  /**
    * @see [[Cats]]
    */
  object AmountExample_FunctorCats extends AmountExample {

    import cats.Functor

    implicit val functor: Functor[Amount] =
      new Functor[Amount] {
        def map[A, B](fa: Amount[A])(f: A => B): Amount[B] =
          fa match {
            case One(a) => One(f(a))
            case Couple(a, b) => Couple(f(a), f(b))
            case Few(a, b, c) => Few(f(a), f(b), f(c))
          }
      }
  }

  object Spec extends org.specs2.mutable.Spec with AutoExamples {

    s"$keyPoint Explicit conversion to $functor applies here:"
    eg {  /** in [[Scalaz]] */

      import scalaz.Functor
      import AmountExample_FunctorScalaz._

      Functor[Amount].map(One(6)) { x: Int => x * 7 } must_== One(42)
    }

    eg { /** in [[Cats]] */
      import cats.Functor
      import AmountExample_FunctorCats._

      Functor[Amount].map(One(6)) { x: Int => x * 7 } must_== One(42)
    }


    s"$keyPoint Implicit conversion to $functor applies here:"

    eq { /** in [[Scalaz]] */
      import scalaz.syntax.functor._
      import AmountExample_FunctorScalaz._

      (One(6): Amount[Int]) map { x: Int => x * 7 } must_== One(42)
    }

    eq { /** in [[Cats]] */
      import cats.syntax.functor._
      import AmountExample_FunctorCats._

      (One(6): Amount[Int]) map { x: Int => x * 7 } must_== One(42)
    }

  }
}
