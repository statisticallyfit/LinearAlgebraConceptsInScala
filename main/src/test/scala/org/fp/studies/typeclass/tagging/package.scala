package org.fp.studies.typeclass

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.specification.dsl.mutable.{AutoExamples, TextDsl}

/**
  * @see [[taggedType]]
  */
package object tagging {


    /**
      * @see [[ann_newType]]
      */
    object Spec extends org.specs2.mutable.Spec with TextDsl with AutoExamples {

      s"$keyPoint ...:".p
      eg { /** in [[Scalaz]] */
//        type Tagged[U] = { type Tag = U }
//        type @@[T, U] = T with Tagged[U]
//
//        import scalaz.@@
//        import scalaz._
//        import scalaz.Tag
//        import scalaz.Tags._
//
//        sealed trait KiloGram
//        def KiloGram[A](a: A): A @@ KiloGram = scalaz.Tag[A, KiloGram](a)
//        val mass = KiloGram(20.0)
        import scalaz.@@

        type KindOfResource = String
        val a : KindOfResource @@ Resource.type = scalaz.Tag[KindOfResource, Resource.type]("abc")

        //@todo
        success
      }

      s"$keyPoint ... ".p
      eg { /** in [[Simulacrum]] */

        //@todo
        success
      }
    }
}
