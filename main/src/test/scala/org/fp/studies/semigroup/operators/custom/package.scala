package org.fp.studies.semigroup.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

import scala.language.higherKinds

//
import org.specs2.specification.dsl.mutable.AutoExamples

//

/**
  *
  */
package object custom {

  object Spec extends org.specs2.mutable.Spec with AutoExamples {

    s"$keyPoint ..."
    s"$bookmarks ..."

    eg { /** in [[Scalaz]] */

     import scalaz.syntax.semigroup._

      success
    }

    eg {
      /** in [[Cats]] */
      //@todo
      success
    }

  }
}
