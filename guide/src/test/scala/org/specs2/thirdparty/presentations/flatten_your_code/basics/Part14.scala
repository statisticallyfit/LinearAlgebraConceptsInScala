package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.fp.resources._
import org.fp.thirdparty.flatten_your_code.snippets.API06

//
import scala.language.higherKinds
//
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Part14 extends UserGuidePage {

  object Includes extends API06

  def is = s"Flatten your code : basics, part 14".title ^ s2"""

In the previous part, we had constructs like `OptionT(Future.successful(theThing))`.

The `OptionT` and `Future.successful` parts are not so interesting, they're just to make the container right.

${Scalaz.md} has a function application operator, that reverses function and parameter.

These are equivalent:

${snippet{

    import scalaz.syntax.id._

    def double(i: Int) = i * 2

    check(double(5) must_== 5 |> double)
  }}

### Exercise

Rewrite the $forComprehension from ${link(Part13)}, but use `|>` for applying `Future.successful` and `EitherT.apply`

${snippet{
    // 8<--
    import Includes._
    // 8<--

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import scalaz.std.scalaFuture.futureInstance
    import scalaz.OptionT
    import scalaz.syntax.id._

    for {
      username <- getUserName(data) |> Future.successful |> OptionT.apply
      user <- getUser(username) |> OptionT.apply
      email = getEmail(user)
      validatedEmail <- validateEmail(email) |> Future.successful |> OptionT.apply
      success <- sendEmail(email) |> OptionT.apply
    } yield success
  }}

Prev ${link(Part13)}
"""
}
