package org.fp.studies.apply.operators

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

import scala.language.higherKinds

//
import org.specs2.specification.dsl.mutable.{TextDsl, AutoExamples}

/**
  *
  */
package object dfault {

  object DefaultSpec extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint The $applyFunctor $operator_apply can have a few equivalent forms:"
    s"$bookmarks $ann_ApplicativeExtractsFunction2"

    eg { /** in [[Scalaz]] */

      s"Option as $applyFunctor".p

      import scalaz.std.option._
      import scalaz.syntax.apply._
      import scalaz.syntax.std.option._

      s"We can use <*>:".p

      9.some <*> {(_: Int) + 3}.some must_== 12.some

      3.some <*> { 9.some <*> {(_: Int) + (_: Int)}.curried.some } must_== 12.some

      s"Another thing I found in 7.0.0-M3 is a new notation that extracts values from containers and apply them to a single function.".p

      s"This is actually useful because for one-function case, we no longer need to put it into the container. " +
        s"I am guessing that this is why Scalaz 7 does not introduce any operator from $applicativeFunctor itself. " +
        s"Whatever the case, it seems like we no longer need Pointed or <$$>.".p

      ^(3.some, 5.some) {_ + _} must_== 8.some

      ^(3.some, scalaz.std.option.none[Int]) {_ + _} must_== scalaz.std.option.none[Int]

      s"The new ^(f1, f2) {...} style is not without the problem though. " +
        s"It doesn’t seem to handle $applicativeFunctor-s that takes two type parameters like Function1, Writer, and Validation. " +
        s"There’s another way called $applicativeBuilder, which apparently was the way it worked in Scalaz 6, got deprecated in M3, " +
        s"but will be vindicated again because of ^(f1, f2) {...}’s issues. " +
        s"Here’s how it looks:".p

      (3.some |@| 5.some) {_ + _} must_== 8.some

      //@todo: elaborate on this point

      val times = {(_: Int) * (_:Double)}

      ^(2.some, Some(3.2))(times) must_== Some(6.4)
      //@todo (Some(1) |@| Some(2)) (times) must_== Some(3)

      /**
        * [[bookmarks]] [[ann_ApplicativeBuilder]]
        */
      import scalaz.std.function._

      val f = ({(_: Int) * 2} |@| {(_: Int) + 10}) {_ + _}

      // (5 * 2) + (5 + 10)
      f(5) must_== 25
    }

    eg {
      /** in [[Cats]] */
      //@todo
      success
    }

    eg { /** in [[Scalaz]] */

      s"List as $applyFunctor".p
      s"LYAHFGG:" +
        s"Lists (actually the list $typeConstructor, []) are $applyFunctor-s. What a surprise!".p

      import scalaz.syntax.apply._
      import scalaz.std.list._

      List(1, 2, 3) <*> List((_: Int) * 0, (_: Int) + 100, (x: Int) => x * x) must_== List(0, 0, 0, 101, 102, 103, 1, 4, 9)

      List(3, 4) <*> { List(1, 2) <*> List({(_: Int) + (_: Int)}.curried, {(_: Int) * (_: Int)}.curried) } must_== List(4, 5, 5, 6, 3, 4, 6, 8)

      (List("ha", "heh", "hmm") |@| List("?", "!", ".")) {_ + _} must_== List("ha?", "ha!", "ha.", "heh?", "heh!", "heh.", "hmm?", "hmm!", "hmm.")

      ^(List(1, 2), List(3)) { _ * _ } must_== List(3, 6)

      (List(1, 2) |@| List(3)) { _ * _ } must_== List(3, 6)
    }

    eg {
      /** in [[Cats]] */
      //@todo
      success
    }

    eg { /** in [[Scalaz]] */

      s"Zip Lists".p
      s"LYAHFGG:" +
        s"However, [(+3),(*2)] <*> [1,2] could also work in such a way that the first function in the left list gets applied to the first value " +
        s"in the right one, the second function gets applied to the second value, and so on. That would result in a list with two values, " +
        s"namely [4,4]. You could look at it as [1 + 3, 2 * 2]." +
        s"This can be done in $Scalaz, but not easily.".p

      import scalaz.Tags
      import scalaz.syntax.apply._
      import scalaz.std.list._
      //import scalaz.syntax.std.list._

      //@todo https://stackoverflow.com/questions/30985397/ziplist-with-scalaz
      //streamZipApplicative.ap(Tags.Zip(Stream(1, 2))) (Tags.Zip(Stream({(_: Int) + 3}, {(_: Int) * 2})))
      //res32: scala.collection.immutable.Stream[Int] with Object{type Tag = scalaz.Tags.Zip} = Stream(4, ?)
      success
    }

    eg {
      /** in [[Cats]] */
      //@todo
      success
    }

    eg { /** in [[Scalaz]] */

      s"Useful functions for Applicatives".p
      s"LYAHFGG:" +
        s"Control.Applicative defines a function that’s called liftA2, which has a type of." +
        s"" +
        s"liftA2 :: (Applicative f) => (a -> b -> c) -> f a -> f b -> f c .".p

      s"There’s Apply[F].lift2:".p

      import scalaz.Apply
      import scalaz.std.option._
      import scalaz.syntax.std.option._

      val lift2 : (Option[Int], Option[List[Int]]) => Option[List[Int]] = Apply[Option].lift2((_: Int) :: (_: List[Int]))

      lift2(3.some, List(4).some) must_== Some(List(3, 4))

      //@todo elaborate
    }

    eg {
      /** in [[Cats]] */
      //@todo
      success
    }

    eg { /** in [[Scalaz]] */

      s"Useful functions for Applicatives".p
      s"LYAHFGG:" +
         s"Let’s try implementing a function that takes a list of $applicativeFunctor-s and returns an $applicativeFunctor that has a list as its " +
        s"result value. We’ll call it sequenceA." +
        s"" +
        s"sequenceA :: (Applicative f) => [f a] -> f [a] " +
        s" sequenceA [] = pure [] " +
        s" sequenceA (x:xs) = (:) <$$> x <*> sequenceA xs  ".p

      s"Let’s try implementing this in Scalaz!".p

      import scalaz.Applicative
      import scalaz.syntax.applicative._

      def sequenceA[F[_]: Applicative, A](list: List[F[A]]): F[List[A]] = list match {
        case Nil     => (Nil: List[A]).point[F]
        case x :: xs => (x |@| sequenceA(xs)) {_ :: _}
      }

      s"Let’s test it:".p

      import scalaz.std.option._
      import scalaz.std.list._
      import scalaz.syntax.std.option._

      sequenceA(List(1.some, 2.some)) must_== Some(List(1, 2))

      sequenceA(List(3.some, scalaz.std.option.none, 1.some)) must_== None

      sequenceA(List(List(1, 2, 3), List(4, 5, 6))) must_==
        List(List(1, 4), List(1, 5), List(1, 6), List(2, 4), List(2, 5), List(2, 6), List(3, 4), List(3, 5), List(3, 6))

      /**
        * @todo: move to the other examples about [[traverseFunctor]]
        */

      s"We got the right answers. " +
        s"What’s interesting here is that we did end up needing Pointed after all, and sequenceA is generic in  $typeClass-y way." +
        s"For Function1 with Int fixed example, we have to unfortunately invoke a dark magic.".p

      import scalaz.std.function._

      type Function1Int[A] = ({type l[A] = (Int) => A})#l[A]

      val functions: List[Function1Int[Int]] = List((_: Int) + 3, (_: Int) + 2, (_: Int) + 1)
      val f: Int => List[Int] = sequenceA[Function1Int, Int] (functions)
      val s = f(3) must_== List(6, 5, 4)

      s"It took us a while, but I am glad we got this far. We’ll pick it up from here later.".p
      s
    }

    eg {
      /** in [[Cats]] */
      //@todo
      success
    }

    s"$keyPoint Applying $applicativeFunctor $operator_LHS and $operator_RHS to extract a projection:"
    eg {
      /** in [[Scalaz]] */

      import scalaz.syntax.apply._
      import scalaz.std.option._
      import scalaz.syntax.std.option._

      1.some <* 2.some must_== Some(1)

      scalaz.std.option.none <* 2.some must_== None

      1.some *> 2.some must_== Some(2)

      scalaz.std.option.none *> 2.some must_== None

    }

    eg { /** in [[Cats]] */

      //@todo
      success
    }

    s"$keyPoint So far, when we were mapping functions over $functor-, we usually mapped functions that take only one parameter. " +
      s"But what happens when we $operator_map a function like *, which takes two parameters, over a $functor?"

    s"$bookmarks $ann_ApplicativeMoreThanFunctor "

    eg { /** in [[Scala]] */

      val times = {(_: Int) * (_:Int)}
      /**
        * @doesnotcompile
        *
        *  List(1, 2, 3, 4) map times
        */

      s"We have to curry the function to $operator_map and we get a list of partial functions:".p
      val partialFuncList = List(1, 2, 3, 4) map times.curried

      s"Now, we can map with the second parameter of 'times' to get the results.".p
      partialFuncList.map(f => f(9)) must_== List(9, 18, 27, 36)
    }

    s"$keyPoint So $applicativeFunctor extends another $typeClass Apply, and introduces $operator_point and its alias 'pure'." +
      s"LYAHFGG: " +
      s"  $operator_point should take a value of any type and return an $applicativeValue with that value inside it. … " +
      s"A better way of thinking about $operator_point would be to say that it takes a value and puts it in some sort " +
      s"of default (or pure) context—a minimal context that still yields that value."

    s"$keyPoint $Scalaz likes the name point instead of pure, and it seems like it’s basically a constructor that takes value A and returns F[A]. " +
      s"It doesn't introduce an operator, but it introduces point method ($operator_point) and its symbolic alias η to all data types."

    s"$bookmarks: $ann_ApplicativeAsTypeConstructor"

    eg { /** in [[Scalaz]] */

      import scalaz.std.list._
      import scalaz.std.option._
      import scalaz.syntax.applicative._

      1.point[List] must_== List(1)
      1.point[List] map {_ + 2} must_== List(3)
      1.η[List] map {_ + 2} must_== List(3)

      1.point[Option] must_== Some(1)
      1.point[Option] map {_ + 2} must_== Some(3)
      1.η[Option] map {_ + 2} must_== Some(3)
    }

    eg { /** in [[Cats]] */

      import cats.instances.list._
      import cats.instances.option._
      import cats.syntax.applicative._

      1.pure[List] must_== List(1)
      1.pure[List] map {_ + 2} must_== List(3)
      //@todo 1.η[List] map {_ + 2} must_== List(3)

      1.pure[Option] must_== Some(1)
      1.pure[Option] map {_ + 2} must_== Some(3)
      //@todo 1.η[Option] map {_ + 2} must_== Some(3)
    }

    s"I can’t really express it in words yet, but there’s something cool about the fact that $constructor is abstracted out.".p

  }
}
