package org.fp.studies.functor

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{AutoExamples, TextDsl}

//
import org.fp.studies.functor.operators.withcustommap.{AmountExample_FunctorScalaz, AmountExample_FunctorCats}

/**
  *
  * [[functorComposition]]
  *
  * @see [[ann_FunctionLifting]]
  */
package object functionlifting {

  object Spec extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint Explicit $functionLifting".p

    eg { /** in [[Scalaz]] */

      import scalaz.Functor
      import AmountExample_FunctorScalaz._

      val timesTwo = (x: Int) => x * 2
      val amountTimesTwo = Functor[Amount].lift(timesTwo)

      amountTimesTwo(Few(1,2,3)) must_== Few(2,4,6)
    }

    eg { /** in [[Cats]] */

      import cats.Functor
      import AmountExample_FunctorCats._

      val timesTwo = (x: Int) => x * 2
      val amountTimesTwo = Functor[Amount].lift(timesTwo)

      amountTimesTwo(Few(1,2,3)) must_== Few(2,4,6)
    }
  }
}
