package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
import org.specs2.specification.core.Env

//
import org.specs2.common.SnippetHelper._
import org.specs2.ugbase.UserGuidePage
//
import org.fp.thirdparty.flatten_your_code.snippets.API06

/**
  *
  */
object Part06 extends UserGuidePage with API06 {

  implicit lazy val ee = Env().executionEnv
  implicit lazy val ec = ee.ec


  def is = s"Flatten your code : basics, part 6".title ^ s2"""

Let's suppose that our API is partially asynchronous

${incl[API06]}

This causes a problem, the $forComprehension desugars to `flatMap`, but we only `flatMap` the outer value.
We can't get to the inner value!

Simplified, given methods `fa` and `fb`, the problem is that `a` and `b` are `Option[Int]`, and not `Int`!

${snippet{
    import scala.concurrent.Future

    val fa: Future[Option[Int]] = ???
    val fb: Future[Option[Int]] = ???

    for {
      a <- fa
      b <- fb

    //} @doesnotcompile yield a - b
    } yield a.get - b.get
  }}

Prev ${link(Part05)}

Next ${link(Part07)}
  """
}
