package org.fp.studies.semigroup.operators

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

  object Spec1 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint Option[Int] forms a $semigroup"
    s"$bookmarks ..."

    eg {
      /** in [[Scalaz]] */

      import scalaz.Semigroup

      s"But needs to be defined in $Scalaz".p
      implicit object IntSemigroup extends Semigroup[Int] {
        def append(f1: Int, f2: => Int): Int = f1 + f2
      }

      import scalaz.std.option._
      import scalaz.syntax.std.option._

      val f_some: Int => Option[Int] = { i => i.some }

      Semigroup[Option[Int]].append(1.some, 2.some)     must_== 3.some
      Semigroup[Option[Int]].append(1.some, f_some(2))  must_== 3.some
      Semigroup[Option[Int]].append(1.some, None)       must_== 1.some

      import scalaz.syntax.semigroup._

      1.some |+| 2.some     must_== 3.some
      1.some |+| f_some(2)  must_== 3.some
      1.some |+| None       must_== 1.some

      s"$bookmarks $ann_Semigroup".p

      import scalaz.std.list._
      List(1, 2, 3) |+| List(4, 5, 6) must_== List(1, 2, 3, 4, 5, 6)

      implicit object StringSemigroup extends Semigroup[String] {
        def append(f1: String, f2: => String): String = f1 + f2
      }
      "abc".some |+| "xyz".some must_== "abxxyz".some
    }

    eg {
      /** in [[Cats]] */

      import cats.Semigroup

      s"But needs to be defined in $Cats as well".p
      implicit object IntSemigroup extends Semigroup[Int] {
        def combine(f1: Int, f2: Int): Int = f1 + f2
      }

      import cats.instances.option._
      import cats.syntax.option._

      val f_some: Int => Option[Int] = { i => i.some }

      Semigroup[Option[Int]].combine(1.some, 2.some)    must_== 3.some
      Semigroup[Option[Int]].combine(1.some, f_some(2)) must_== 3.some
      Semigroup[Option[Int]].combine(1.some, None)      must_== 1.some

      import cats.syntax.semigroup._

      1.some |+| 2.some     must_== 3.some
      1.some |+| f_some(2)  must_== 3.some
      1.some |+| None       must_== 1.some

      s"$bookmarks $ann_Semigroup".p

      import cats.instances.list._
      List(1, 2, 3) |+| List(4, 5, 6) must_== List(1, 2, 3, 4, 5, 6)

      implicit object StringSemigroup extends Semigroup[String] {
        def combine(f1: String, f2: String): String = f1 + f2
      }
      "abc".some |+| "xyz".some must_== "abxxyz".some
    }
  }

  object Spec2 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint Map[A, B] forms a $semigroup if `B` forms a $monoid"
    s"$bookmarks ..."

    eg { /** in [[Scalaz]] */

      import scalaz.std.map._
      import scalaz.std.anyVal._
      import scalaz.syntax.semigroup._

      s"So, Map[*, Int] forms a $semigroup".p
      val map1 = Map(1 -> 9,   2 -> 20)
      val map2 = Map(1 -> 100, 3 -> 300)

      val mergedMap: Map[Int, Int] = Map(1 -> 109, 2 -> 20, 3 -> 300)
      map1 |+| map2      must_== mergedMap
      map1.⊹(map2)       must_== mergedMap
      map1.mappend(map2) must_== mergedMap
    }

    eg {
      /** in [[Cats]] */

      //do not import cats.instances.map._
      import cats.instances.all._
      import cats.syntax.semigroup._

      s"So, Map[*, Int] forms a $semigroup".p
      val map1 = Map(1 -> 9,   2 -> 20)
      val map2 = Map(1 -> 100, 3 -> 300)

      val mergedMap: Map[Int, Int] = Map(1 -> 109, 2 -> 20, 3 -> 300)

      map1 |+| map2      must_== mergedMap
      map1.combine(map2) must_== mergedMap
    }

    s"$keyPoint Map[A, B] forms a $semigroup even if `B` forms $semigroup"
    eg {
      /** in [[Scalaz]] */

      import scalaz.std.map._
      import scalaz.syntax.semigroup._

      val map1 = Map(1 -> "a",  2 -> "b")
      val map2 = Map(1 -> "xy", 3 -> "c")

      val mergedMap: Map[Int, String] = Map(1 -> "axy", 2 -> "b", 3 -> "c")

      import scalaz.Semigroup
      implicit object StringSemigroup extends Semigroup[String] {
        def append(f1: String, f2: => String): String = f1 + f2
      }

      map1 |+| map2      must_== mergedMap
      map1.⊹(map2)       must_== mergedMap
      map1.mappend(map2) must_== mergedMap
    }

    eg {
      /** in [[Cats]] */

      import cats.instances.map._
      import cats.syntax.semigroup._

      val map1 = Map(1 -> "a",  2 -> "b")
      val map2 = Map(1 -> "xy", 3 -> "c")

      val mergedMap: Map[Int, String] = Map(1 -> "axy", 2 -> "b", 3 -> "c")

      import cats.Semigroup
      implicit object StringSemigroup extends Semigroup[String] {
        def combine(f1: String, f2: String): String = f1 + f2
      }

      map1 |+| map2      must_== mergedMap
      // '⊹' is not supported
      map1.combine(map2) must_== mergedMap
    }
  }
}
