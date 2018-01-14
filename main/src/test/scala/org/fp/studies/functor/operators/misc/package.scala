package org.fp.studies.functor.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}

  /**
    *
    * @see [[operator_void]], [[operator_Fproduct]]
    */
package object misc {

  /**
    * @see [[Scalaz]]
    */
  object Spec extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint The $operator_Fproduct pairs a value with the result of applying a function to that value.".p
    s"$bookmarks: $ann_FProductEx1, $ann_FProductEx2"

    eg { /** in [[Scalaz]] */
      // 8<--
      import scalaz.std.list._
      import scalaz.syntax.functor._
      // 8<--
      val len: String => Int = _.length

      List("a",      "aa",      "b",       "ccccc").fproduct(len).toMap must_==
       Map("a" -> 1, "aa" -> 2, "b" ->  1, "ccccc" -> 5)
    }

    eg { /** in [[Cats]] */
      // 8<--
      import cats.instances.list._
      import cats.syntax.functor._
      // 8<--
      val len: String => Int = _.length

      List("a",       "aa",      "b",       "ccccc").fproduct(len).toMap must_==
        Map("a" -> 1, "aa" -> 2, "b" ->  1, "ccccc" -> 5)
    }

    s"$keyPoint The $operator_void transforms F[A] into a F[Unit].".p
    eg { /** in [[Scalaz]] */
      // 8<--
      import scalaz.Functor
      import scalaz.std.option._
      // 8<--

      Functor[Option].void(Some(1)) must_== Some(())
    }

    eg { /** in [[Cats]] */
      // 8<--
      import cats.Functor
      import cats.instances.option._
      // 8<--

      Functor[Option].void(Some(1)) must_== Some(())
    }

    s"$keyPoint The $operator_as transforms F[A] into a F[B].".p
    eg { /** in [[Scalaz]] */
      import scalaz.std.list._
      import scalaz.syntax.functor._
      // 8<--

      List("a", "b", "c").as("-") must_== List("-", "-", "-")
    }

    s"$keyPoint The $operator_as transforms F[A] into a F[B].".p
    eg { /** in [[Cats]] */
      import cats.instances.list._
      import cats.syntax.functor._
      // 8<--

      List("a", "b", "c").as("-") must_== List("-", "-", "-")
    }

    s"$keyPoint $functor also enables some operators that override the values in the data structure " +
      s"like $operator_shift, $operator_as, $operator_Fpair, $operator_strengthL, $operator_strengthR, and $operator_void:".p

    eg { /** in [[Scalaz]] */
      import scalaz.syntax.functor._
      import scalaz.std.list._

      List(1, 2, 3) >| "x" must_== List("x", "x", "x")
      List(1, 2, 3) as "x" must_== List("x", "x", "x")
      List(1, 2, 3) strengthL "x" must_== List(("x",1), ("x",2), ("x",3))
      List(1, 2, 3) strengthR "x" must_== List((1, "x"), (2, "x"), (3, "x"))
      List(1, 2, 3).fpair must_== List((1,1), (2,2), (3,3))
      List(1, 2, 3).void must_== List((), (), ())

    }

    eg { /** in [[Cats]] */
      import cats.syntax.functor._
      import cats.instances.list._

      //@todo List(1, 2, 3) >| "x" must_== List("x", "x", "x")
      List(1, 2, 3) as "x" must_== List("x", "x", "x")
      //@todo List(1, 2, 3) strengthL "x" must_== List(("x",1), ("x",2), ("x",3))
      //@todo List(1, 2, 3) strengthR "x" must_== List((1, "x"), (2, "x"), (3, "x"))
      //@todo List(1, 2, 3).fpair must_== List((1,1), (2,2), (3,3))
      List(1, 2, 3).void must_== List((), (), ())
    }
  }
}
