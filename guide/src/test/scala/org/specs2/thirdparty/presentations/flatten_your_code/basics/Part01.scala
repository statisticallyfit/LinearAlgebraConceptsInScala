package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._
//
import org.specs2.ugbase.UserGuidePage
import org.specs2.common.SnippetHelper._
//
import org.fp.thirdparty.flatten_your_code.snippets.API01

/**
  *
  */
object Part01 extends UserGuidePage with API01 {

  def is = s"Flatten your code : basics, part 1".title ^ s2"""

These are the service methods from which we're going to build a program.
We'll reuse these five methods in all the parts, although they will evolve a bit.

${incl[API01]}

The program that we're making is also very similar in all the parts.
We're getting a username from a Map with data, then we lookup the corresponding user.
We get the email address from the user, validate it, and send an email to it.

Not great: nested maps and flatMaps!

  ${snippet{
    val result1 = getUserName(data).map { username =>
      getUser(username).map { user =>
        val email = getEmail(user)
        validateEmail(email).map { validatedEmail =>
          sendEmail(validatedEmail)
        }
      }
    }
  }}

Exercise, rewrite the above as a $forComprehension

Solution:

${snippet{

    for {
      username  <- getUserName(data)
      user      <- getUser(username)
      email     =  getEmail(user) // String type has no a `flatMap`
      _         <- validateEmail(email)
      result    <- sendEmail(email)

    } yield result
  }}

Next: ${link(Part02)}
    """
}
