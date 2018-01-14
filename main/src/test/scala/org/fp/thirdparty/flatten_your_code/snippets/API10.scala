// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

import scala.language.higherKinds

/**
  *
  */
trait API10 {

  object Code10 {
    // 8<--
    trait Monad[F[_]] {
      def map[A, B](container: F[A])(function: A => B): F[B]
      def flatMap[A, B](container: F[A])(function: A => F[B]): F[B]
      def create[B](value: B): F[B]
    }
    // 8<--
  }
}
// 8<--
