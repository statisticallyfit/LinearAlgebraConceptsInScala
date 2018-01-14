package org.fp.studies.applicative.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

import scala.language.higherKinds

//
import org.specs2.specification.dsl.mutable.AutoExamples
import org.fp.studies.apply.operators.custom.{AmountExample_ApplyScalaz, AmountExample_ApplyCats}

//

/**
  *
  */
package object custom {

     object Spec extends org.specs2.mutable.Spec with AutoExamples {

          /**
            * [[ann_applicativeLaws]]
            * @todo
            */
          eg {
               import scalaz.Applicative
               import scalaz.std.option._
               import scalaz.syntax.applicative._




               //@todo
               success
          }

          s"$keyPoint $applicativeFunctor enable $operator_sequence to transform lists of $functor-s:"
          eg {
               /** in [[Scalaz]] */

               import scalaz.Applicative
               import scalaz.syntax.applicative._
               import AmountExample_ApplyScalaz._

               //Applicative[Amount].sequence(List(One(2), One(3), One(7))) must_== One(List(2,3,7))
               //List(One(2), One(3), One(7)).sequence must_== One(List(2,3,7))
               def sequenceA[F[_]: Applicative, A](list: List[F[A]]): F[List[A]] = list match {
                    case Nil     => (Nil: List[A]).point[F]
                    case x :: xs => (x |@| sequenceA(xs)) {_ :: _}
               }
               sequenceA[Amount, Int](List(One(2), One(3), One(7))) must_== One(List(2,3,7))
          }

          eg { /** in [[Cats]] */
               import cats.Applicative
               import cats.syntax.applicative._
               import AmountExample_ApplyCats._

               //@todo
               success
          }

     }

}
