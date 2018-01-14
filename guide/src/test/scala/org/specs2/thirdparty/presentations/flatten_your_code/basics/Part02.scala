package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.specs2.common.SnippetHelper._
import org.specs2.ugbase.UserGuidePage
//
import org.fp.thirdparty.flatten_your_code.snippets.API02

/**
  *
  */
object Part02 extends UserGuidePage with API02 {

  def is = s"Flatten your code : basics, part 2".title ^ s2"""

In Part 1, our service methods returned `Option`, and we composed a program that returned an `Option` as well.

But `Option` is seldomly sufficient, no error reporting!

For example, it's impossible to distinguish between a user not being found or the email address failing to validate.

So, instead of `Option`, we can use `Either`, which supports a failure value.

${incl[API02]}

Scala `Either` doesn't have `map` or `flatMap` methods.
But you can create a 'right projection', which does have them!

${snippet{
    val result1 = getUserName(data).right.flatMap { username =>
      getUser(username).right.flatMap { user =>
        val email = getEmail(user)
        validateEmail(email).right.flatMap { validatedEmail =>
          sendEmail(validatedEmail)
        }
      }
    }

    success
  }}

So using right projections, you can use `Either` in a for comprehension:

${snippet{

    for {
      username <- getUserName(data).right
      user <- getUser(username).right
    } yield user

    success
  }}

However, there is a problem! `RightProjection`s `map` and `flatMap` methods don't return a new `RightProjection`, but an `Either`.
And often a for-comprehension will desugar to chained `maps` or `flatMaps`, and that breaks:

For example this:

```
  for {
    username <- getUserName(data).right
    userNameUpper = username.toUpperCase
  } yield userNameUpper
```

Will desugar to something similar to getUserName(data).right.map{...}.map{...}
But after the first `map`, you get an Either, which doesn't have a `map` method, so the second map breaks.

So our program that we could change to using for-comprehensions while it consisted of `Option`s, can't be
changed to for comprehensions when it uses `Either`.

This doesn't work:

${snippet{
    for {
      username <- getUserName(data).right
      user <- getUser(username).right
      //illTyped("""email = getEmail(user)""")
      validatedEmail <- validateEmail("no email").right
      result <- sendEmail("no email").right

    } yield result

  }}

Prev ${link(Part01)}

Next ${link(Part03)}
    """
}
