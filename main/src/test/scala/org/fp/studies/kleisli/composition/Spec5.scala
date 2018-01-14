package org.fp.studies.kleisli.composition

import org.fp.bookmarks._
import org.fp.concepts._
import org.fp.resources._
//
import org.specs2.specification.Snippets

/**
  * @todo determine how to verify the $snippets
  *
  */
object Spec5 extends org.specs2.Specification with Snippets /*with TextDsl*/ {

     case class Make(id: Int, name: String)

     case class Part(id: Int, name: String)

     import scalaz.NonEmptyList

     val part1 = Part(1, "Gear Box")
     val part2 = Part(2, "Clutch cable")

     /**
       *
       */
     object SomeFunctions1 {

          val make: (Int) => Make = (_) => Make(1, "Suzuki")

          val parts: Make => List[Part] = {
               case Make(1, _) => List(part1, part2)
          }
     }

     /**
       *
       */
     object SomeFunctions2 {

          import scalaz.syntax.std.boolean._

          val make  = (x: Int) => (x == 1).option(Make(1, "Suzuki"))

          val parts = (x: Make) =>
               (x.id == 1).option(NonEmptyList(part1, part2))
     }

     /**
       *
       */
     object SomeFunctions3 {

          import scalaz.syntax.std.boolean._

          val make  = (x: Int) => (x == 1).option(Make(1, "Suzuki"))

          val parts: Make => List[Part] = {
               case Make(1, _) => List(part1, part2)
               case _ => Nil
          }
     }


     override def is = s"Composing programs in a few different ways".title ^ s2"""

Today I’ll be exploring a few different ways in which you can compose programs.

The examples that follow all deal with Vehicles - more specifically makes and parts.

$bookmarks

So we have a function `Int => Make` and then a function : `Make => List[Part]`.
From set theory we know this implies we must have a function : `Int => List[Part]`.
This is nothing more than simple $functionComposition:

  ${snippet{
          /** [[Scala]] */
          import SomeFunctions1._

          val f = parts compose make
          f(1) must_== List(part1, part2)
     }.offsetIs(-4)}

Alternatively you can use $operator_andThen which works like $operator_compose, but with the arguments flipped:

  ${snippet{
          /**@todo */
          import SomeFunctions1._

          val g = make andThen parts
          g(1) must_== List(part1, part2)
     }.offsetIs(-4)}

Now we have a function make: `Int => Option[Make]` and a function parts: `Make => Option[NonEmptyList[Part]]`.
Based on our first example we should have a way to create a function from `Int => Option[NonEmptyList[Part]]`.

This isn’t immediately obvious however.

While `make` does return a `Make`, it is wrapped inside an `Option` so we need to account for a possible failure.
This leads to our first attempt:

  $bookmarks
  ${snippet{
          /** [[Scalaz]] */
          import SomeFunctions2._
          import scalaz.NonEmptyList

          val f: Option[Make] => Option[NonEmptyList[Part]] = {
               case Some(m) => parts(m)
               case _ => None
          }
          val g = f compose make
          g(1) must_== Some(NonEmptyList(part1, part2))
     }.offsetIs(-4)}

While this works, we had to manually create the plumbing between the two functions. You can imagine that with different return and input types,
this plumbing would have to be rewritten over and over.

All the function `f` above is doing is serving as an adapter for `parts`.
It turns out there is a couple of ways in which this pattern can be generalised.

#### Composition through $monadicBind:

Option is a $monad so we can define `f` using a $forComprehension:

  ${snippet{
          /** [[Scalaz]] */
          import scalaz.NonEmptyList
          import SomeFunctions2._

          val f = (x: Int) => for {
               m <- make(x)
               p <- parts(m)
          } yield p

          f(1) must_== Some(NonEmptyList(part1, part2))
     }.offsetIs(-4)}

Which is simply syntactic sugar for:

  ${snippet{
          import SomeFunctions2._

          val f = (x: Int) => for {
               m <- make(x)
               p <- parts(m)
          } yield p

          val g = make(_: Int) flatMap (m => parts(m).map(p => p))
          g(1) must_== f(1)
     }.offsetIs(-4)}

You can also use the symbolic alias for $operator_bind, which makes it a lot nicer.

  ${snippet{
          import scalaz.syntax.bind._
          import scalaz.std.option._
          import SomeFunctions2._

          val f = (x: Int) => for {
               m <- make(x)
               p <- parts(m)
          } yield p

          val h = make(_: Int) >>= parts
          h (1) must_== f(1)
     }.offsetIs(-4)}

The reason this is better is that `make` and `parts` could operate under a different $monad but the client code would not need to change.

In the example below, we’re operating under the `List` $monad:

  ${snippet{
          /** [[Scalaz]] */
          val words: (String) => List[String] = _.split( """\s""").toList
          val chars: String => List[Char] = _.toList

          val f = (phrase: String) => for {
               m <- words(phrase)
               p <- chars(m)
          } yield p

          val charList = List('M', 'o', 't', 'o', 'r', 'c', 'y', 'c', 'l', 'e', 's', 'a', 'r', 'e', 'f', 'u', 'n', 't', 'o', 'r', 'i', 'd', 'e', '!')
          f ("Motorcycles are fun to ride!") must_== charList

          s"or even:"//.p
          val g = words(_: String) flatMap (w => chars(w).map(c => c))

          g ("Motorcycles are fun to ride!") must_== charList
     }.offsetIs(-4)}

We used the exact same $forComprehension syntax to compose these operations. This works because both `Option` and `List` are $monad

Notwithstanding, this still feels like unnecessary plumbing. All we are doing with the $forComprehension / $operator_flatMap is extracting the values from their
respective $monad-s to simply put them back in. It would be nice if we could simply do something like `make compose parts` as we did in our first example.

#### Composition through $KleisliArrow-s

A $KleisliArrow is simply a wrapper for a function of type `A => F[B]`. This is the same type of the second argument to the $monadicBind as defined in ${Scalaz.is}:

```
  bind[A, B](fa: F[A])(f: A => F[B]): F[B]
```

By creating a $KleisliArrow from a function, we are given a function that knows how to extract the value from a $monad F and feed it into the underlying function,
much like $operator_bind does, but without actually having to do any binding yourself.

To use a concrete example, let’s create a $KleisliArrow from our `parts` function:

  ${snippet{
          import SomeFunctions3._
          import scalaz.Kleisli._

          kleisli(parts)
     }.offsetIs(-4)}

You can read this type as being a function which knows how to get a value of type Make from the Option $monad and will ultimately return an `Option[NonEmptyList[Part]]`.
Now you might be asking, why would we want to wrap our functions in a $KleisliArrow?
By doing so, you have access to a number of useful functions defined in the $KleisliArrow trait, one of which is `<==<` (aliased as $operator_composeK):

This gives us the same result as the version using the $forComprehension but with less work and with code that looks similar to simple $functionComposition.

  ${snippet{
          /** [[Scalaz]] */

          import scalaz.NonEmptyList
          import scalaz.std.option._
          import scalaz.Kleisli._

          import SomeFunctions2._


          val f1 = kleisli(parts) <==< make
          val f2 = kleisli(parts) composeK make

          f1(1) must_== Some(NonEmptyList(part1, part2))
          f2(1) must_== Some(NonEmptyList(part1, part2))
     }.offsetIs(-4)}

#### Not there yet

One thing that was bugging me is the return type for parts above:

```
  Make => Option[NonEmptyList[Part]]
```

Sure this works but since lists already represent non-deterministic results, one can make the point that the Option type there is reduntant since, for this example,
we can treat both None and the empty List as the absence of result.

Let’s update the code:

  ${snippet{
          /** [[Scalaz]] */

          import scalaz.syntax.std.boolean._

          val make = (x: Int) => (x == 1).option(Make(1, "Suzuki"))

          val parts: Make => List[Part] = {
               case Make(1, _) => List(part1, part2)
               case _ => Nil
          }
     }.offsetIs(-4)}

It seems we’re in worse shape now! As before, `parts`’s input type doesn’t line up with `make`’s return type. Not only that, they aren’t even in the same $monad anymore!
This clearly breaks our previous approach using a $KleisliArrow to perform the composition.
On the other hand it makes room for another approach: $functorLifting.

### Composition through $functorLifting

In Scala - and category theory - $monad-s are $functor-s. As such both Option and List have access to a set of useful $functor combinators.
The one we’re interested in is called $operator_lift.

Say you have a function `A => B` and you have a `functor F[A]`. Lifting is the name of the operation that transforms the function `A => B` into a function of type `F[A] => F[B]`.
This sounds useful.

Here are our function types again:

```
    make: Int => Option[Make]
    parts: Make => List[Part]
```

We can’t get a function `Int => List[Part]` because make returns an `Option[Make]` meaning it can fail.

We need to propagate this possibility in the composition. We can however lift parts into the `Option` $monad, effectively changing its type from `Make => List[Part]` to `Option[Make] => Option[List[Part]]:`

  ${snippet{
          import scalaz.std.option._
          import scalaz.Functor
          import SomeFunctions3._

          val f = Functor[Option].lift(parts) compose make
          f(1) must_== Some(List(part1, part2))
     }.offsetIs(-4)}

`f` now has the type `Int => Option[List[Part]]` and we have once again successfully composed both functions without writing any plumbing code ourselves.

Mark pointed out to me that $operator_lift is pretty much the same as $operator_map but with the arguments reversed.
So the example above can be more succintly expressed as:

  $bookmarks
  ${snippet{
          import SomeFunctions3._

          val g = make(_: Int).map(parts)
          g(1) must_== Some(List(part1, part2))
     }.offsetIs(-4)}
    """
}