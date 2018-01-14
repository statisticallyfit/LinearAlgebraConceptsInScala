// 8<---
package org.fp.studies.typeclass.definition.auto

/**
  *
  */
trait Definitions {

// 8<---
  import simulacrum._

  @typeclass trait CanTruthy[A] { self =>
    /** Return true, if `a` is truthy. */
    def truthy(a: A): Boolean
  }

  object CanTruthy {
    def fromTruthy[A](f: A => Boolean): CanTruthy[A] = new CanTruthy[A] {
      def truthy(a: A): Boolean = f(a)
    }
  }
}
// 8<---
object Definitions extends Definitions
