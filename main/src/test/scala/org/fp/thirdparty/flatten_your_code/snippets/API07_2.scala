// 8<--
package org.fp.thirdparty.flatten_your_code.snippets

/**
  *
  */
trait API07_2 {

  object Code07_2 {

    import scala.concurrent.ExecutionContext.Implicits.global
    // 8<--
    import scala.concurrent.Future

    case class FutureOption[A](contents: Future[Option[A]]) {
      def flatMap[B](fn: A => FutureOption[B]) = FutureOption {
        contents.flatMap {
          case Some(value) => fn(value).contents
          case None => Future.successful(None)
        }
      }

      def map[B](fn: A => B) = FutureOption {
        contents.map { option =>
          option.map(fn)
        }
      }
    }
    // 8<--
  }
}
// 8<--
