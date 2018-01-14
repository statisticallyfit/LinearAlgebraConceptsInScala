package org.fp.studies.monoid

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{AutoExamples, TextDsl}

/**
  * @see [[monoid]]
  */
package object dfault {


  /**
    * @see [[Scalaz]]
    * @see [[operator_append]]
    */
  object Spec extends org.specs2.mutable.Spec with TextDsl with AutoExamples {

    s"$keyPoint Use $monoid-s to abstract over $operator_append-ing things of a same kind (using the explicit syntax):".p
    eg {
      import scalaz._, std.anyVal._

      Monoid[Int].append(10,20) must_== 30
    }

    eg { /** in [[Cats]] */
      /*@todo
            import cats.Monoid
            import cats.instances.anyVal._
            import cats.syntax.m

            Monoid[Int].append(10,20) must_== 30
      */
      success
    }

    s"$keyPoint Use $monoid-s to abstract over $operator_append-ing things of a same kind (using the sugar syntax):".p

    eg { /** in [[Scalaz]] */
      import scalaz._, syntax.semigroup._, std.anyVal._

      10 |+| 20 must_== 30
    }

    /**
      * @see [[ann_unboxNewTypesInScalaz]]
      */
    s"$keyPoint Use multiple $operator_append-s for the same $monoid-s:".p
    eg {
      import scalaz._, syntax.semigroup._, std.anyVal._
      import Tags._

      Multiplication(2) |+| Multiplication(5) must_== Monoid[Int].multiply(2,5)
    }

    eg { /** in [[Cats]] */
      /*@todo
            import cats._, syntax.semigroup._, std.anyVal._

            10 |+| 20 must_== 30
      */
      success
    }
  }

}
