package org.fp.studies.semigroup

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}
import org.specs2.ScalaCheck
import org.specs2.common.CheckedSpec

/**
  *
  * @see [[lawIdentity]], [[lawComposition]]
  */
package object laws {

  object Spec extends org.specs2.mutable.Spec with AutoExamples with TextDsl with ScalaCheck with CheckedSpec {

    s"$keyPoint The only law for is a $semigroup is $lawAssociativity".p
    s"see $lawIdentity"

    eg {
      /** in [[Scalaz]] */

      import scalaz.scalacheck.ScalazProperties.semigroup
      import scalaz.std.anyVal._

      import org.scalacheck.{Gen, Arbitrary}
      implicit val arbMyType: Arbitrary[Int] = Arbitrary(Gen.choose(1, 20))
      check(semigroup.laws[Int])
    }

    eg {
      /** in [[Cats]] */

      import cats.Semigroup
      import cats.instances.all._
      import cats.kernel.laws.GroupLaws

      import org.scalacheck.{Gen, Arbitrary}
      implicit val arbMyType: Arbitrary[Int] = Arbitrary(Gen.choose(1, 20))

      val rs1 = GroupLaws[Int].semigroup(Semigroup[Int])
      check(rs1.all)
    }
  }
}
