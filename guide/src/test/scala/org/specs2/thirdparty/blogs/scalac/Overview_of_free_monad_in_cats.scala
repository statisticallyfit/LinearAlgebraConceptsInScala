//package org.specs2.thirdparty.blogs.scalac
//
//import org.fp.concepts._
//import org.fp.resources._
//import org.fp.bookmarks._
//
//import scala.language.higherKinds
//
////
//import org.specs2.ugbase.UserGuidePage
//import org.specs2.common.SnippetHelper._
//import org.specs2.execute.SnippetParams
//
//import org.fp.thirdparty.scalac.Overview_of_free_monads_in_cats.snippets.{API01, API02, API03}
//
///**
//  *
//  * @see [[freeMonad]], [[coProduct]]
//  */
//
///**
//  *
//  */
//object Overview_of_free_monad_in_cats extends UserGuidePage {
//  def is = s"Overview of free monad in cats".title ^ s2"""
//
//## Putting the Free monad to work
//
//We gonna implement basic instructions from LOGO language - the famous “Turtle”.
//We will implement movement in a 2 dimensional space.
//
//### Creating set of instructions (AST)
//
//Ok, let’s say we have some set of instructions that we want to use in order to create program.
//
//Using the LOGO example I would like to implement basic functionality like moving forward and backward,
//rotating left or right and showing the current position.
//To do that I’ve created a few simple classes to represent those actions.
//
//These are just simple case classes. Type A will be the type that $freeMonad will be working on.
//That means if we use a $operator_flatMap we will have to provide a function of type `A => Instruction[B]`.
//
//${incl[API01]}
//
//There are also two classes that will be used. It’s a part of simplified domain model.
//
//${snippet {
//   /**/
//   case class Position(x: Double, y: Double, heading: Degree)
//   case class Degree(private val d: Int) {
//     val value = d % 360
//   }
//}}
//
//At this point we have defined only our actions with some additional types, but it does not compute anything, it’s abstract.
//
//## Creating the DSL
//
//DSL is a Domain Specific Language. It contains functions that refer to the domain. Such functions make the code more readable
//and expressive. To make it easier to use I’ve created some helper methods that will create $freeMonad-s wrapping our actions,
//by lifting `Instruction[A]` into `Free[Instruction, A]`.
//
//${incl[API02]}
//
//These methods are used to write the application and make it stateless by taking a position as a parameter and not storing it inside the function.
//
//## The program
//
//Now we can easily use our DSL. Having the methods in place we can use them in a $forComprehension to build a program description.
//Let’s say we want to get from `point (0,0)` to `point (10,10)`. The code looks like this:
//
//${snippet{
//    /**/
//    // 8<--
//    import API02._
//    import API02.Logo._
//    //import API02.dsl._
//    import org.specs2.matcher.{EitherBaseMatchers => _, _}
//
//    // 8<--
//    import cats.free.Free
//
//    val program: (Position => Free[Instruction, Position]) = {
//      start: Position =>
//        for {
//          p1 <- forward(start, 10)
//          p2 <- right_(p1, Degree(90))
//          p3 <- forward(p2, 10)
//        } yield p3
//    }
//}}
//
//As we can see the program is just a function that takes a starting position and moves the turtle.
//The result of calling the function is also a $freeMonad and that’s great news because it’s very easy to compose another function using this one.
//We can simply think of creating functions to draw basic geometric shapes and then using these functions to draw something more complicated;
//furthermore, it’s going to compose very well. It’s like that because of referential transparency property of the $freeMonad.
//
//Another advantage is that we don’t worry how it is computed. We are just expressing how program should work using the DSL.
//
//## Interpreter
//
//At this moment we have our program description, but we want to execute it. In order to do that a $naturalTransformation is needed.
//It’s a function that can transform one $typeConstructor into another one. A $naturalTransformation from `F` to `G` is often written as `F[_] ~> G[_]`.
//What is important, is that we have to provide implicit conversion from `G` to a `Monad[G]`.
//
//It’s possible to have multiple interpreters, e.g. one for testing and another one for production.
//This is very useful when we create an AST for interacting with a database or some other external service.
//The simplest $monad is the identity monad Id, which is defined in ${Cats.md} as a simple type container.
//
//${snippet{
//    type Id[A] = A
//  }}
//
//It has all necessary functions implemented and implicit value that is a `Monad[Id]`.
//In the ${Cats.md} package object there is an implicit instance that helps with conversion to an Id.
//
//Let’s use it to write our own interpreter.
//
//${snippet{
//    /**/
//    // 8<--
//    import API02._
//    import API02.Logo._
//    //import API02.dsl._
//
//    //type Id[A] = A
//    // 8<--
//    import cats.{Id, ~>}
//    import cats.free.Free
//
//    object InterpreterId extends (Instruction ~> Id) {
//      //import Computations._
//
//      override def apply(fa: Instruction[Position]): Id[Position] = fa match {
//
//        case Forward(p, length) => forward(p, length)
//        case Backward(p, length) => backward(p, length)
//        case RotateLeft(p, degree) => left_(p, degree)
//        case RotateRight(p, degree) => right_(p, degree)
//        case ShowPosition(p) => println(s"showing position $p"); p
//      }
//    }
//  }}
//
//`InterpreterId` is defined as a $naturalTransformation from `Instruction` to `Id`.
//`Computations` object has all the functions that are necessary to compute a new position.
//Functions used in first 4 cases return value of type `Position` which is equal to `Id[Position]`.
//The `Id` does not modify value, it is just a container that provides $monadicFunction-s.
//If you put `Position` value `pos` into `Id[Position].pure(pos)` it will return value `pos` of type `Position`.
//If you want to $operator_map over the `Id` using function `f: Position => B`, it will behave the same as if you apply the `pos` into the `f`.
//
//### Running the program
//
//To run the program we need to simply $operator_foldMap it using the interpreter and pass a parameter to the function to start evaluation.
//
//${snippet{
//    // 8<--
//    import API02._
//    import API02.Logo._
//    //import API02.dsl._
//
//    import cats.free.Free
//
//    val program: (Position => Free[Instruction, Position]) = {
//      start: Position =>
//        for {
//          p1 <- forward(start, 10)
//          p2 <- right_(p1, Degree(90))
//          p3 <- forward(p2, 10)
//        } yield p3
//    }
//    // 8<--
////@todo
////    val startPosition = Position(0.0, 0.0, Degree(0))
////
////    program(startPosition).foldMap(InterpreterId)
//  }}
//
//The result of this operation will be just a `Position` because last the instruction of program was forward and we yielded the result of it.
//When we look at the definition of `Forward` we can see that it extends `Instruction[Position]` and that type parameter specifies that we will
//have a value of `Position` type as result.
//
//### Another Interpreter
//
//Let’s assume we want to move on a surface that has only non-negative coordinates. Whenever the value of `x` or `y` in `Position` becomes negative we want to stop further computation.
//
//The simplest solution is to change the interpreter so that it’ll transform `Instruction` into `Option`.
//Function $operator_foldMap is using the $operator_flatMap of the $monad that our `Instruction` is transformed into.
//So if we going to return `None` then the computation will be stopped.
//Let’s implement it.
//
//${snippet{
//    // 8<--
//    import API02._
//    import API02.Logo._
//    //import API02.dsl._
//
//    import cats.{Id, ~>}
//    import cats.free.Free
//    // 8<--
//
//    object InterpretOpt extends (Instruction ~> Option) {
//      //import Computations._
//      val nonNegative: (Position) => Option[Position] = {
//        p => if (p.x >= 0 &&p.y >= 0) Some(p) else None
//      }
//
//      override def apply (fa: Instruction[Position]) = fa match {
//        case Forward(p, length) => nonNegative(forward(p, length))
//        case Backward(p, length) => nonNegative(backward(p, length))
//        case RotateLeft(p, degree) => Some(left_(p, degree))
//        case RotateRight(p, degree) => Some(right_(p, degree))
//        case ShowPosition(p) => Some(println(s"showing position $p")); p
//      }
//    }
//  }}
//
//We defined `nonNegative` as a part of the interpreter - the reason is that it’s an implementation detail not connected to the business logic.
//And now if we run the program with such definition
//
//${snippet{
//    // 8<--
//    import cats.{Id, ~>}
//    import cats.free.Free
//
//    import API02._
//    import API02.Logo._
//    //import API02.dsl._
//    // 8<--
//
//    val program2: (Position => Free[Instruction, Unit]) = {
//      s: Position =>
//        for {
//          p1 <- forward(s, 10)
//          p2 <- right_(p1, Degree(90))
//          p3 <- forward(p2, 10)
//          p4 <- backward(p3, 20)//Here the computation stops, because result will be None
//          _  <- showPosition(p4)
//        } yield ()
//    }
//  }}
//
//It’ll not print the position, so we achieved our goal.
//
//We can easily think about another interpreter that could be implemented for this AST. It could be a graphical representation of our program.
//
//### Composing
//
//$freeMonad-s are a really powerful tool. One of the reasons is composition.
//
//Our example is rather simple, but you can imagine that you’ve built a whole instruction set for your business logic.
//It is completely separated from other code in application.
//Then you can add set of instructions for logging, accessing the database, failure handling or just another business logic but for some reason separated from the previous one.
//Each of such DSLs can be easily tested.
//Writing the program doesn’t change much. You have to do some adjustments.
//
//Let’s say we want to add to our LOGO application two new instructions - `PencilUp` and `PencilDown`, but they will be in other instruction set.
//First thing is defining `PencilInstruction`.
//
//${snippet{
//    // 8<--
//    import API02._
//    // 8<--
//
//    sealed trait PencilInstruction[A]
//    case class PencilUp(position: Position) extends PencilInstruction[Unit]
//    case class PencilDown(position: Position) extends PencilInstruction[Unit]
//
//  }}
//
//The problem with the program type is that `PencilInstruction` and `Instruction` don’t have a common supertype, and they shouldn’t have.
//We need to define some type that will be either former or latter of these two type constructors and still be able to pass them type parameter.
//
//Luckily, there is a $coProduct, which is made exactly for such tasks. It’s a wrapper for `Xor` type ($disjunction), which is more or less the same thing as Scala’s `Either`.
//
//$coProduct requires two $typeConstructor-s and type that will be inserted into them. In this way we can make superset of two sets of instructions and create a supertype for them.
//
//${snippet{
//    import cats.data.Xor
//
//    final case class Coproduct[F[_], G[_], A](run: F[A] Xor G[A])
//  }}
//
//Let’s define our common type.
//
//${snippet{
//
//    // 8<--
//    import API02._
//    import API02.Logo._
//
//    sealed trait PencilInstruction[A]
//    case class PencilUp(position: Position) extends PencilInstruction[Unit]
//    case class PencilDown(position: Position) extends PencilInstruction[Unit]
//    // 8<--
//
//    import cats.data.Coproduct
//    type LogoApp[A] = Coproduct[Instruction, PencilInstruction, A]
//  }}
//
//In application we will be using `LogoApp` as a whole set of instructions.
//To make the mixing of these two ASTs possible we need to be able to lift both of them to $coProduct type.
//
//To do this we have to change our lifting methods - instead of using `Free.liftF` method we will use an injecting function.
//
//${snippet{
//    import cats.free.{Free, Inject}
//
//    final class FreeInjectPartiallyApplied[F[_], G[_]] private[free] {
//      def apply[A](fa: F[A])(implicit I : Inject[F, G]): Free[G, A] =
//        Free.liftF(I.inj(fa))
//    }
//
//    def inject[F[_], G[_]]: FreeInjectPartiallyApplied[F, G] = new FreeInjectPartiallyApplied
//
//  }}
//
//It basically means that we can lift our `Instruction` or `PencilInstruction` set into the $coProduct which is the superset of both of them.
//
//Because we want to be flexible about the $coProduct types we will define classes wrapping our DSL.
//These classes will take type parameter, which will be corresponding to the $coProduct.
//
//This is what our Logo definition will look like
//
//${incl[API02]}
//
//Moves and PencilActions will be implicitly needed in our program.
//They gonna be parametrized by our `LogoApp` type, and will have all methods lifted to `Free` that will be operating on the `LogoApp`.
//
//That means we can mix them in one $forComprehension expression. Now our program definition will look like this:
//
//${snippet{
//    // 8<--
//    import cats.{Id, ~>}
//    import cats.free.Free
//    import cats.data.Coproduct
//
//    import API03._
//    import API03.Logo._
//    //import API03.Logo.dsl._
//
//    type LogoApp[A] = Coproduct[Instruction, PencilInstruction, A]
//    // 8<--
//
//    def program(implicit M: Moves[LogoApp], P: PencilActions[LogoApp]): (Position => Free[LogoApp, Unit]) = {
//      import M._, P._
//      s: Position =>
//        for {
//          p1 <- forward(s, 10)
//          p2 <- right(p1, Degree(90))
//          _  <- pencilUp(p2)
//          p3 <- forward(p2, 10)
//          _  <- pencilDown(p3)
//          p4 <- backward(p3, 20)
//          _  <- showPosition(p4)
//        } yield ()
//    }
//  }}
//
//""".stripMargin
//
//  implicit override def snippetParams[T]: SnippetParams[T] = defaultSnippetParameters[T].copy(evalCode = true).offsetIs(-4)
//}
