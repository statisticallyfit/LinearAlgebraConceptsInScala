package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.fp.resources._

//
import org.specs2.common.SnippetHelper._
import org.specs2.ugbase.UserGuidePage
//
import org.fp.thirdparty.flatten_your_code.snippets.API03

/**
  *
  */
object Part03 extends UserGuidePage with API03 {

  def is = s"Flatten your code : basics, part 3".title ^ s2"""

So Scala's `Either` is pretty useless for what we want.

This is caused by `Either` being _unbiased_. It equally values the Left and Right sides, so it
believes that `map` doesn't make sense without specifying which side you want to map.
That's why you need a left or right projection before you can map an `Either`.

Luckily, ${Scalaz.md} also has an either-like type! Classname: `\/`.
Looks like the mathematical symbol for disjunction, which is the same thing.

And \/ is right-biased, so it has `map` and `flatMap` methods, and they work on the right side.

So the type `Either[String, Int]` would, using ${Scalaz.md}, be `\/[String, Int]`

But you can use infix notation for the type and write this as: String \/ Int
The data constructors are `\/-` and `-\/` instead of `Right` and `Left`.

Here are our service methods again, but now changed to return a ${Scalaz.md} `\/`.

${incl[API03]}

### Exercise

Write the same program as in ${link(Part01)} and ${link(Part02)}, with a $forComprehension:

Now we can get rid of the `.right` that we had on Scala's `Either`, because `\/` is right-biased.

### Solution


${snippet{
    for {
      username        <- getUserName(data)
      user            <- getUser(username)
      email            = getEmail(user)
      validatedEmail  <- validateEmail(email)
      result          <- sendEmail(email)

    } yield result

  }}

Prev ${link(Part02)}

Next ${link(Part04)}
    """
}
