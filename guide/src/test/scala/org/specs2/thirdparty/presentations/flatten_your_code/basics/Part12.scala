package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.fp.resources._

import scala.language.higherKinds

//
import org.specs2.common.SnippetHelper._
import org.specs2.ugbase.UserGuidePage
//
import org.fp.thirdparty.flatten_your_code.snippets.API10

/**
  *
  */
object Part12 extends UserGuidePage {

  object Includes extends API10

  def is = s"Flatten your code : basics, part 12".title ^ s2"""

In the previous parts we've built our own `FutureOption` container, and then generalized that
to an `AnyMonadOption` container, that combines `Option` with any other $monad.

${Scalaz.md} defines a similar thing, and calls it $optionTransformer. The class is named `OptionT`.

Similarly, there is also an `EitherT`, an $eitherTransformer, which combines a `\/` (so not a `scala.Either`) with any other $monad.

$keyPoint Keep in mind that an `OptionT` works when the `Option` is the *inner* container, so `Future[Option[A]]`, and not `Option[Future[A]]`.
Similarly, an `EitherT` works with `Future[String \/ A]` or `Option[Throwable \/ A]` but not `String \/ Option[A]`.

The $monad $typeClass that we defined looks like:

${incl[API10]}

$keyPoint But it turns out that we can define `map` in terms of `flatMap` and `create`.

${snippet{
/**/
    trait Monad2[F[_]] {
      def flatMap[A, B](container: F[A])(function: A => F[B]): F[B]
      def create[B](value: B): F[B]

      def map[A, B](container: F[A])(f: A => B): F[B] = flatMap(container)(f andThen create)
    }
  }}

This means that if we define an instance, we only need to define `flatMap` and `create`.

 * `FlatMap` is called `bind` in ${Scalaz.md}.
 * It also often has the ${operator_>>=}.
 * `Create` is called $operator_point in ${Scalaz.md}.

We called the implicit $monadInstance parameter `monadInstanceForF`.
${Scalaz.md} typically call these instances the same as the type they are for:

 * Our version:          `(implicit monadInstanceForF: Monad[F])`
 * Typical ${Scalaz.md}: `(implicit F: Monad[F])`

${Scalaz.md} calls the 'contents' parameter 'run', so you can do: `myTransformer.run` to get the original structure out.

Prev ${link(Part11)}

Next ${link(Part13)}
  """
}
