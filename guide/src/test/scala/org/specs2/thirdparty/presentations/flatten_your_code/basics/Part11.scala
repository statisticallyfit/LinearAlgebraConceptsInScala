package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._

import scala.language.higherKinds

//
import org.fp.thirdparty.flatten_your_code.snippets.{API07_2, API10}
import org.specs2.common.SnippetHelper._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Part11 extends UserGuidePage {

  object Includes extends API07_2 with API10

  def is = s"Flatten your code : basics, part 11".title ^ s2"""

Here's `FutureOption` again:

${incl[API07_2]}

We've seen that we only use the properties of a $monad from the `Future` here, so we can generalize `FutureOption`:

${incl[API10]}

${snippet{
/**/
    // 8<--
    import Includes.Code10._
    // 8<--

    case class AnyMonadOption[F[_], A](contents: F[Option[A]])(implicit monadInstanceForF: Monad[F]) {
      def flatMap[B](fn: A => AnyMonadOption[F, B]) = AnyMonadOption[F, B] {
        monadInstanceForF.flatMap(contents){
          case Some(value) => fn(value).contents
          case None => monadInstanceForF.create(None)
        }
      }

      def map[B](fn: A => B) = AnyMonadOption[F, B] {
        monadInstanceForF.map(contents){ option =>
          option.map(fn)
        }
      }
    }
  }}

$keyPoint So now you can not only use it for `Future[Option[Int]`, but for any other outer $monad as well, like `Option[Option[Int]]` or `String \/ Option[Int]`

Prev ${link(Part11)}

Next ${link(Part12)}
  """
}
