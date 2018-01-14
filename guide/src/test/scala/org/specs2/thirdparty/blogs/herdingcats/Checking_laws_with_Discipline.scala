package org.specs2.thirdparty.blogs.herdingcats

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

import scala.language.higherKinds

//
import org.specs2.ugbase.UserGuidePage
import org.specs2.execute.SnippetParams
import org.specs2.execute.Snippet._

/**
  *
  * @see [[lawIdentity]], [[lawComposition]]
  */

/**
  *
  */
object Checking_laws_with_Discipline extends UserGuidePage {
  def is = s"Checking laws with Discipline".title ^ s2"""

The compiler can't check for the laws, but ${Cats.md} ships with a `FunctorLaws` trait that describes this in code
https://github.com/typelevel/cats/blob/6785e6f856dc08fa31081013be27345aa5fe6d8e/laws/src/main/scala/cats/laws/FunctorLaws.scala:

${snippet{
    /**/
    import cats.Functor
    import cats.syntax.functor._
    import cats.laws.{InvariantLaws, IsEq, IsEqArrow}

    /**
      * Laws that must be obeyed by any [[functor]].
      */
    trait FunctorLaws[F[_]] extends InvariantLaws[F] {
      implicit override def F: Functor[F]

      def covariantIdentity[A](fa: F[A]): IsEq[F[A]] =
        fa.map(identity) <-> fa

      def covariantComposition[A, B, C](fa: F[A], f: A => B, g: B => C): IsEq[F[C]] =
        fa.map(f).map(g) <-> fa.map(f andThen g)
    }

    object FunctorLaws {
      def apply[F[_]](implicit ev: Functor[F]): FunctorLaws[F] =
        new FunctorLaws[F] { def F: Functor[F] = ev }
    }
}}

## Checking laws from the REPL

This is based on a library called ${Discipline.md}, which is a wrapper around ${ScalaCheck.md}.
We can run these tests from the REPL with ${ScalaCheck.md}.

${snippet{
    /**/
    import cats.instances.all._
    import cats.laws.discipline.FunctorTests

    val rs = FunctorTests[Either[Int, ?]].functor[Int, Int, Int]
    check(rs.all)
}}

`rs.all` returns `org.scalacheck.Properties`, which implements check method.

## Checking laws with ${Discipline.md} + ${Specs2.md}

You can also bake your own cake pattern into a test framework of choice. Here's for ${Specs2.md}:


${snippet{
    /**/
    import org.specs2.Specification
    import org.typelevel.discipline.specs2.Discipline
    import cats.instances.AllInstances
    import cats.syntax.AllSyntax

    trait CatsSpec extends Specification with Discipline with AllInstances with AllSyntax
}}

${Cats.md}' source include one for ScalaTest.
The spec to check the $functorLaws for `Either[Int, Int]` looks like this:

${snippet{
    /**/
    // 8<--
    import org.specs2.Specification
    import org.typelevel.discipline.specs2.Discipline
    import cats.instances.AllInstances
    import cats.syntax.AllSyntax

    trait CatsSpec extends Specification with Discipline with AllInstances with AllSyntax
    // 8<--

    import cats.laws.discipline.FunctorTests

    class EitherSpec extends CatsSpec { def is = s2"""

      Either[Int, ?] forms a functor $e1

      """

      def e1 = checkAll("Either[Int, Int]", FunctorTests[Either[Int, ?]].functor[Int, Int, Int])
    }

    // 8<--
    run(new EitherSpec)
    // 8<--
}}

The `Either[Int, ?]` is using ${KindProjector.md}.


## Breaking the law

### LYAHFGG:
  <i>Let's take a look at a pathological example of a type constructor being an instance of the $functor $typeClass but not really
  being a $functor, because it doesn't satisfy the laws.</i>

Let's try breaking the law.

${snippet {
    /**/
    import cats._

    sealed trait COption[+A]
    case class CSome[A](counter: Int, a: A) extends COption[A]
    case object CNone extends COption[Nothing]

    object COption {
      implicit def coptionEq[A]: Eq[COption[A]] = new Eq[COption[A]] {
        def eqv(a1: COption[A], a2: COption[A]): Boolean = a1 == a2
      }

      implicit val coptionFunctor = new Functor[COption] {
        def map[A, B](fa: COption[A])(f: A => B): COption[B] =
          fa match {
            case CNone => CNone
            case CSome(c, a) => CSome(c + 1, f(a))
          }
      }
    }

    // <$--$> Here's how we can use this
    import cats.syntax.functor._

    check((CSome(0, "ho"): COption[String]).map(identity) must_== CSome(1, "ho"))
}}

This breaks the first law because the result of the identity function is not equal to the input.
To catch this we need to supply an "arbitrary" `COption[A]` implicitly:

${snippet{
// 8<-- start
    /**/
    import cats._

    sealed trait COption[+A]
    case class CSome[A](counter: Int, a: A) extends COption[A]
    case object CNone extends COption[Nothing]

    object COption {
      implicit def coptionEq[A]: Eq[COption[A]] = new Eq[COption[A]] {
        def eqv(a1: COption[A], a2: COption[A]): Boolean = a1 == a2
      }

      implicit val coptionFunctor = new Functor[COption] {
        def map[A, B](fa: COption[A])(f: A => B): COption[B] =
          fa match {
            case CNone => CNone
            case CSome(c, a) => CSome(c + 1, f(a))
          }
      }
    }
// 8<-- end

    // <$--$> Here's how we can use this:

    import cats.syntax.functor._

    (CSome(0, "ho"): COption[String]) map {
      identity
    } must_== CSome(1, "ho")

    import org.scalacheck.{ Arbitrary, Gen }

    // 8<-- start
    import cats.laws.discipline.FunctorTests
    import org.specs2.Specification
    import org.typelevel.discipline.specs2.Discipline
    import cats.instances.AllInstances
    import cats.syntax.AllSyntax

    trait CatsSpec extends Specification with Discipline with AllInstances with AllSyntax
    // 8<-- end

    class COptionSpec extends CatsSpec {
      implicit def coptionArbiterary[A](implicit arbA: Arbitrary[A]): Arbitrary[COption[A]] =
        Arbitrary {
          val arbSome = for {
            i <- implicitly[Arbitrary[Int]].arbitrary
            a <- arbA.arbitrary
          } yield (CSome(i, a): COption[A])
          val arbNone = Gen.const(CNone: COption[Nothing])
          Gen.oneOf(arbSome, arbNone)
        }

      def is = s2"""
  COption[Int] forms a functor                             $e1
  """

      def e1 = checkAll("COption[Int]", FunctorTests[COption].functor[Int, Int, Int])
    }

    // 8<--
    run(new COptionSpec)
    // 8<--
}}

The tests failed as expected.

"""
  implicit override def snippetParams[T]: SnippetParams[T] = {
    defaultSnippetParameters[T].copy(evalCode = true, asCode = markdownCode(multilineQuotes = inlineText))
  }

  /**
    * A big hack to break the markdown code snippet at the poin(s0 when text lines occur
    */
  def inlineText = (code: String) => {
    val sep = "<$--$>"
    val mardownSep = "|```"
    val markdownBlockDelim = s"\n$mardownSep\n"

    s"""$mardownSep
        |${code.split("\n").map(l =>
            if (l.contains(sep))
              markdownBlockDelim +
                  l.substring(l.indexOf(sep) + sep.length) +
              markdownBlockDelim
            else
              l).mkString("\n") }
        $mardownSep
     """.stripMargin
  }

}
