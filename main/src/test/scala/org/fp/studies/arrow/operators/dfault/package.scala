package org.fp.studies.arrow.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}

/**
  *
  */
package object dfault {

  /**
    *
    */
  object Spec1 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    object SomeFunctions {
      val plus1 = (_: Int) + 1
      val times2 = (_: Int) * 2
      val rev = (_: String) reverse

    }

    s"$keyPoint Example of using $FunctionArrow with Function1"

    s"$bookmarks $ann_Arrow1".p
    eg {
      /** [[Scalaz]] */

      import scalaz.syntax.arrow._
      import scalaz.std.AllInstances._ //@todo more granular imports

      import SomeFunctions._

      s"Applying first on the $FunctionArrow.".p
      (plus1.first apply (7, "abc")) must_=== (8, "abc")

      s"Applying second on the $FunctionArrow.".p
      (plus1.second apply ("def", 14)) must_=== ("def", 15)

      s"Combine plus1 and rev on the $FunctionArrow to apply across both elements of a pair.".p
      (plus1 *** rev apply (7, "abc")) must_=== (8, "cba")

      s"Perform both plus1 and times2 on a value using the $FunctionArrow".p
      (plus1 &&& times2 apply 7) must_=== (8, 14)

      s"Perform plus1 on a pair using the $FunctionArrow".p
      (plus1.product apply (9, 99)) must_=== (10, 100)
    }

    eg {
      /** [[Cats]] */

      //@todo
      success
    }

  }
}
