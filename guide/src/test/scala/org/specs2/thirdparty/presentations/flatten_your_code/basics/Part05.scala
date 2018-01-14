package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
import org.specs2.specification.core.Env

//
import org.specs2.ugbase.UserGuidePage
//
import org.fp.thirdparty.flatten_your_code.snippets.API04

/**
  *
  */
object Part05 extends UserGuidePage with API04 {

  implicit lazy val ee = Env().executionEnv
  implicit lazy val ec = ee.ec

  def is = s"Flatten your code : basics, part 5".title ^ s2"""

There's nothing special about the types you can use in a $forComprehension. The Scala compiler will happily desugar for you,
and as long as the type you work with has the proper `map` and `flatMap` methods where required, it will be fine.

For $forComprehension-s with no `yield`, the last step gets desugared to `foreach`.

And if you use guards, like this:

```
for {
    foo <- bar if quux
  }
````
Then a `withFilter` method is required.

Future has a `flatMap` and `map` method as well, so can also be used in a $forComprehension.

${snippet{
/**/
    import scala.concurrent.Future

    val fa = Future(3)
    val fb = Future(5)

      for {
        a <- fa
        b <- fb
      } yield a + b
  }}

### Debugging!

Lines in a $forComprehension *must* be a generator (`<-`) or a value definition (`=`).
What if we just want to `println` an intermediate value?

${snippet{
/**/
    for {
      a <- Option(3)
      b <- Option(5)
      _ = println("A = " + a)
      c <- Option(11)
    } yield a + b + c

  }}

If your yield gets unwieldy, you can also just assign it to a value.

${snippet{
/**/
    for {
      a <- Option(3)
      b <- Option(5)
      c <- Option(11)
      result = a + b + c
    } yield result
  }}

Prev ${link(Part05)}

Next ${link(Part06)}
  """
}
