// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

/**
  *
  */
trait API07 {

  object Code07 {

    import scala.concurrent.ExecutionContext.Implicits.global
    // 8<--
    import scala.concurrent.Future

    case class FutureOption[A](contents: Future[Option[A]]) {

      def flatMap[B](fn: A => FutureOption[B]): FutureOption[B] = FutureOption {
        contents.flatMap {
          ???
        }
      }

      def map[B](fn: A => B): FutureOption[B] = ???
    }
    // 8<--
  }
}
// 8<--
