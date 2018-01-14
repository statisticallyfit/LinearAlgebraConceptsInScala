package org.fp.studies.applicative.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

import scala.language.higherKinds

//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}

/**
  *
  */
package object dfault {

  object Spec extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint ..."

    s"$bookmarks: ..."

    eg { /** in [[Scalaz]] */

      import scalaz.std.list._
      import scalaz.std.option._
      import scalaz.syntax.applicative._

      //@todo
      success
    }

    eg { /** in [[Cats]] */

      import cats.instances.list._
      import cats.instances.option._
      import cats.syntax.applicative._

      //@todo
      success
    }
  }
}
