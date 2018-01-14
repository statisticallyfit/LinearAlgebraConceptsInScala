package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._

import scala.language.higherKinds

//
import org.specs2.ugbase.UserGuidePage
import org.specs2.common.SnippetHelper._
import org.fp.thirdparty.flatten_your_code.snippets.{API10, API07_2}

/**
  *
  */
object Part10 extends UserGuidePage {

  object Includes extends API07_2 with API10

  def is = s"Flatten your code : basics, part 10".title ^ s2"""

Here's `FutureOption` again:

${incl[API07_2]}

Note that from the outer container, `Future`, we only use the following:
 - The `map` method
 - The `flatMap` method
 - Creating a new one: `Future.successful`

As it turns out, these functionalities are part of a $typeClass called $monad:

${incl[API10]}

${snippet{
/**/
    // 8<--
    import Includes.Code10._
    // 8<--
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global

    // Instance for Future:
    val futureMonad = new Monad[Future] {
      override def map[A, B](container: Future[A])(function: A => B) = container.map(function)
      override def flatMap[A, B](container: Future[A])(function: A => Future[B]) = container.flatMap(function)
      override def create[B](value: B) = Future.successful(value)
    }

  }}

### Exercise

Create a $monad instance for `Option`

${snippet{
    // 8<--
    import Includes.Code10._
    // 8<--

    val optionMonad = new Monad[Option] {
      override def map[A, B](container: Option[A])(function: A => B) = container.map(function)
      override def flatMap[A, B](container: Option[A])(function: A => Option[B]) = container.flatMap(function)
      override def create[B](value: B) = Some(value)
    }
  }}

Prev ${link(Part09)}

Next ${link(Part11)}
  """
}
