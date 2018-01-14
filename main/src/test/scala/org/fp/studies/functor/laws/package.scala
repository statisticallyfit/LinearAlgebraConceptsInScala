package org.fp.studies.functor

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}

/**
  *
  * @see [[lawIdentity]], [[lawComposition]]
  */
package object laws {

  object Spec extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint Mapping Identity function leaves the $functor unchanged ".p
    s"see $lawIdentity"

    eg { /** in [[Scalaz]] */
      import scalaz.Functor
      import scalaz.syntax.functor._
      import scalaz.std.list._

      val identity: Int => Int = x => x

      import org.scalacheck.Arbitrary
      val anyList:List[Int] = Arbitrary.arbitrary[List[Int]].sample.get
      anyList map identity must_== anyList
    }

    eg { /** in [[Cats]] */
      import cats.syntax.functor._
      import cats.instances.list._

      import org.scalacheck.Arbitrary
      val anyList:List[Int] = Arbitrary.arbitrary[List[Int]].sample.get
      anyList map identity must_== anyList
    }

    s"$keyPoint Mapping a composed function on a $functor is same as the mapping the functions one by one ".p
    s"see $lawComposition"

    eg { /** in [[Scalaz]] */
      import scalaz.Functor
      import scalaz.{std, syntax}
      import syntax.functor._
      import std.list._
      import std.function._

      val f = (_: Int) * 3
      val g = (_: Int) + 1

      import org.scalacheck.Arbitrary
      val anyList:List[Int] = Arbitrary.arbitrary[List[Int]].sample.get

      Functor[List].map(anyList)(f map g) must_== Functor[List].map(anyList)(f).map(g)
    }

    eg { /** in [[Cats]] */
      import cats.Functor
      import cats.{instances, syntax}
      import syntax.functor._
      import instances.list._
      import instances.function._

      val f = (_: Int) * 3
      val g = (_: Int) + 1

      import org.scalacheck.Arbitrary
      val anyList:List[Int] = Arbitrary.arbitrary[List[Int]].sample.get

      Functor[List].map(anyList)(f map g) must_== Functor[List].map(anyList)(f).map(g)
    }
  }
}