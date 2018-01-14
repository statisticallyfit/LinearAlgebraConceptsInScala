package org.fp.studies.functor.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{AutoExamples, TextDsl}

/**
  *
  * [[operator_map]]
  *
  */
package object withdefaultmap {

  /**
    * @see [[ann_FunctionSyntax]]
    */
  object Spec extends org.specs2.mutable.Spec with TextDsl with AutoExamples {

    s"$keyPoint Examples for $functor-s like Option and List ".p
    s"$bookmarks: $ann_functorsOptionAndList"

    eg { /** in [[Scalaz]] */
      import scalaz.{Functor, std}

      val len: String => Int = _.length

      import std.option._
      Functor[Option].map(Some("adsf"))(len)           must_== Some(4)
      Functor[Option].map(None)(len)                   must_== None

      import std.list._
      Functor[List]  .map(List("qwer", "adsfg"))(len)  must_== List(4,5)
      Functor[List]  .map(List(1, 2, 3))(_ * 2)        must_== List(2, 4, 6)
    }

    eg { /** in [[Cats]] */
      import cats.{Functor, instances}
      val len: String => Int = _.length

      import instances.option._
      Functor[Option].map(Some("adsf"))(len)           must_== Some(4)
      Functor[Option].map(None)(len)                   must_== None

      import instances.list._
      Functor[List]  .map(List("qwer", "adsfg"))(len)  must_== List(4,5)
      Functor[List]  .map(List(1, 2, 3))(_ * 2)        must_== List(2, 4, 6)
    }

    s"$keyPoint Either is not really a $functor but a $biFunctor".p

    eg { /** in [[Scalaz]] */
      import scalaz.{Functor, std}
      import scalaz.syntax.functor._
      import std.either._
      val increment: Int => Int = i => i + 1 // a bit of help for Intellij type inference
      (Right(1): Either[String, Int]) map increment must_== Right(2)
    }

    eg { /** in [[Cats]] */
      import cats.{Functor, instances}
      import cats.syntax.functor._
      import cats.instances.either._
      val increment: Int => Int = i => i + 1 // a bit of help for Intellij type inference
      (Right(1): Either[String, Int]) map increment must_== Right(2)
    }
  }
}

