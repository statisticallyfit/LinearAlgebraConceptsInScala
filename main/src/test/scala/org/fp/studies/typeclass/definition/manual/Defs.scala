// 8<---
package org.fp.studies.typeclass.definition.manual

/**
  *
  */
trait Defs {

// 8<---
  trait CanTruthy[A] {
    self =>
    /** @return true, if `a` is truthy. */
    def truthys(a: A): Boolean
  }

  object CanTruthy {
    def apply[A](implicit ev: CanTruthy[A]): CanTruthy[A] = ev

    def fromTruthy[A](f: A => Boolean): CanTruthy[A] = new CanTruthy[A] {
      def truthys(a: A): Boolean = f(a)
    }

    trait CanTruthyOps[A] {
      def self: A

      implicit def F: CanTruthy[A]

      final def truthy: Boolean = F.truthys(self)
    }

    object ops {
      implicit def toCanIsTruthyOps[A](v: A)(implicit ev: CanTruthy[A]) =
        new CanTruthyOps[A] {
          def self = v

          implicit def F: CanTruthy[A] = ev
        }
    }
  }

// 8<---
}

object Defs extends Defs


