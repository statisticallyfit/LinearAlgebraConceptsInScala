package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.fp.resources._
//
import scala.language.higherKinds
//
import org.specs2.common.SnippetHelper._
import org.specs2.ugbase.UserGuidePage
//
import org.fp.thirdparty.flatten_your_code.snippets.API06

/**
  *
  */
object Part13 extends UserGuidePage {

  object Includes extends API06

  def is = s"Flatten your code : basics, part 13".title ^ s2"""

In parts ${link(Part08)} to ${link(Part12)}, we've derived a $monadTransformer, a class that transforms two containers
into a single container, that can be conveniently used in a $forComprehension.

${Scalaz.md} has a bunch of $monadTransformer-s as well. With two nested containers, like `Future[Option[A]]`,
you need the $monadTransformer that corresponds with the inner container, in this case the $optionTransformer, which is called OptionT in ${Scalaz.md}.

A small example: here, `a` and `b` are `Int`, extracted from both the `Future` and the `Option`!

${snippet{
/**/
    import scalaz.OptionT
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    import scalaz.std.scalaFuture.futureInstance

     val fa: Future[Option[Int]] = ???
     val fb: Future[Option[Int]] = ???

     val finalOptionT = for {
        a <- OptionT(fa)
        b <- OptionT(fb)
      } yield a + b

     // And to get back to the normal structure:
     val finalFutureOption: Future[Option[Int]] = finalOptionT.run

     finalFutureOption
    }}

Back to our good old service methods, where some are now asynchronous: returning a `Future`.

${incl[API06]}

Again, we want to use them in a $forComprehension.

Now we need to do two things:
1) Upgrade the `Option[A]` values to `Option[Future[A]]`, so they're all the same container type
2) Put all `Option[Future[A]]` into an $optionTransformer, OptionT.

### Exercise: Make a $forComprehension

  ${snippet {
      // 8 <--
      import Includes._
      // 8 <--
      import scalaz.OptionT
      import scala.concurrent.Future
      import scala.concurrent.ExecutionContext.Implicits.global
      import scalaz.std.scalaFuture.futureInstance

      for {
        username <- OptionT(Future.successful(getUserName(data)))
        user <- OptionT(getUser(username))
        email = getEmail(user)
        validatedEmail <- OptionT(Future.successful(validateEmail(email)))
        result <- OptionT(sendEmail(email))
      } yield result
    }}

$keyPoint We finally have a compatible containers inside the $forComprehension.

Prev ${link(Part12)}

Next ${link(Part14)}
"""
}
