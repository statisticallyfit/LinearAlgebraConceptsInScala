package org.fp.studies.functor

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}

/**
  *
  * @see [[functorComposition]]
  */
package object composition {

  /**
    * Note the [[org.fp.resources.Scalaz]] dual syntax for function composition: 'map and '∘'
    */
  object Spec extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint Compose a function f : A => B with g : B => C by first lifting into a $functor:".p
    eg { /** in [[Scalaz]] */

      val inc = (x: Int) => x + 1
      val timesTwo = (x: Int) => x * 2

      val func1 = (x: Int) => x.toDouble
      val func2 = (y: Double) => y * 2

      import scalaz.{std, syntax}
      import std.function._
      import syntax.functor._

      s"Function1 can be thought as infinite $operator_map from the domain to the range. ".p
      s"$bookmarks: $ann_Function1asMap".p
      ((inc map timesTwo) (3): Int) must_== 8

      s"Order of composition for the $operator_map is opposite to regular 'compose'".p
      s"$bookmarks: $ann_MapOperatorCompositionOrder".p
      ((inc compose timesTwo) (3): Int) must_== 7

      val func3 = func1 map func2
      (func3(1) : Double) must_== 2.0
    }

    eg { /** in [[Cats]] */

      val inc = (x: Int) => x + 1
      val timesTwo = (x: Int) => x * 2

      val func1 = (x: Int) => x.toDouble
      val func2 = (y: Double) => y * 2

      import cats.syntax.functor._
      import cats.instances.function._

      s"Function1 can be thought as infinite $operator_map from the domain to the range. ".p
      s"$bookmarks: $ann_Function1asMap".p
      ((inc map timesTwo) (3): Int) must_== 8

      s"Order of composition for the $operator_map is opposite to regular 'compose'".p
      s"$bookmarks: $ann_MapOperatorCompositionOrder".p
      ((inc compose timesTwo) (3): Int) must_== 7

      val func3 = func1 map func2
      (func3(1) : Double) must_== 2.0
    }

    s"$keyPoint Compose a $functor-s $operator_map with the ${Scala.md} collection map function:".p

    eg { /** in [[Scalaz]] */
      val func1 = (x: String) => x.length
      val func2 = (y: Int) => y > 0

      import scalaz.{std, syntax}
      import std.function._
      import syntax.functor._

      val func3 = func1 map func2

      (func3("abc") : Boolean) must_== true

      List("abc", "", "def") map func1 map func2 must_== List(true, false, true)
      List("abc", "", "def") map func3           must_== List(true, false, true)
    }

    eg { /** in [[Cats]] */

      val func1 = (x: String) => x.length
      val func2 = (y: Int) => y > 0

      import cats.syntax.functor._
      import cats.instances.function._

      val func3 = func1 map func2

      (func3("abc") : Boolean) must_== true

      List("abc", "", "def") map func1 map func2 must_== List(true, false, true)
      List("abc", "", "def") map func3           must_== List(true, false, true)
    }


    s"$keyPoint Compose any two $functor-s F[_] and G[_] to create a new $functor F[G[_]]:".p
    eg { /** in [[Scalaz]] */

      import scalaz.Functor
      import scalaz.std
      import std.option._
      import std.list._

      val listOpt = Functor[List] compose Functor[Option]

      listOpt.map(List(Some(1), None, Some(3)))(_ + 1) must_== List(Some(2), None, Some(4))
    }

    eg { /** in [[Cats]] */

      import cats.Functor
      import cats.instances.option._
      import cats.instances.list._

      val listOpt = Functor[List] compose Functor[Option]

      listOpt.map(List(Some(1), None, Some(3)))(_ + 1) must_== List(Some(2), None, Some(4))
    }

  }
}
