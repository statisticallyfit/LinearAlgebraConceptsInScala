package org.fp.studies.kleisli

import org.fp.concepts._
import org.fp.resources._
import org.fp.bookmarks._

//
import org.specs2.specification.dsl.mutable.{AutoExamples, TextDsl}

/**
  *
  * @see [[functionComposition]]
  */
package object composition {

  /**
    *
    */
  object Spec1 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    object Catnip {

      implicit class IdOp[A](val a: A) extends AnyVal {
        def some: Option[A] = Some(a)
      }
      def none[A]: Option[A] = None
    }

    s"$keyPoint Composing monadic functions" +
      s"LYAHFGG:" +
      s"When we were learning about the $monadLaws, we said that the $$operator_<=< function is just like $functionComposition, " +
      s"only instead of working for normal functions like a -> b, it works for $monadicFunction-s like a -> m b.".p

    eg { /** in [[Scalaz]] */

      import scalaz.Kleisli._

      import scalaz.std.option._
      import Catnip._

      val f = kleisli { (x: Int) => (x +   1).some }
      val g = kleisli { (x: Int) => (x * 100).some }

      s"There’s a special wrapper for a function of type A => F[B] called $KleisliArrow:".p

      s"We can then compose the functions using $operator_compose, which runs the right-hand side first:".p
      (4.some flatMap (f compose g).run) must_== Some(401)
      (4.some flatMap (f <==<    g).run) must_== Some(401)

      s"There’s also $operator_andThen, which runs the left-hand side first:".p
      (4.some flatMap (f andThen g).run)       must_== Some(500)
      (4.some flatMap (f >=>     g).run)       must_== Some(500)
                      (f >=>     g) =<< 4.some must_== Some(500)

      s"Both $operator_compose and $operator_andThen work like $functionComposition but note that they retain the $monadicContext.".p

      s"$KleisliArrow also has some interesting methods like $operator_lift, which allows you to lift a $monadicFunction " +
        s"into another $applicativeFunctor.".p
      import scalaz.std.list._
      //import scalaz.std.function._

      val l = f.lift[List]

      (List(1, 2, 3) flatMap l.run) must_== List(Some(2), Some(3), Some(4))
      //@todo (l =<< List(1, 2, 3))         must_== List(Some(2), Some(3), Some(4))
    }

    s"$bookmarks $ann_Kleisli"
    eg { /** in [[Cats]] */

      import cats.data.Kleisli
      import cats.syntax.flatMap._

      import cats.instances.option._
      import Catnip._

      val f = Kleisli { (x: Int) => (x + 1).some }
      val g = Kleisli { (x: Int) => (x * 100).some }

      s"There’s a special wrapper for a function of type A => F[B] called $Kleisli:".p

      s"We can then compose the functions using 'compose', which runs the right-hand side first:".p
      (4.some >>= (f compose g).run) must_== Some(401)

      s"There’s also 'andThen', which runs the left-hand side first:".p
      (4.some >>= (f andThen g).run) must_== Some(500)

      s"Both 'compose' and 'andThen' work like $functionComposition but note that they retain the monadic context.".p

      s"$Kleisli also has some interesting methods like $operator_lift, which allows you to lift a $monadicFunction " +
        s"into another $applicativeFunctor.".p

      import cats.instances.list._

      val l = f.lift[List]

      (List(1, 2, 3) flatMap l.run) must_== List(Some(2), Some(3), Some(4))
      (List(1, 2, 3) >>=     l.run) must_== List(Some(2), Some(3), Some(4))
    }
  }

  /**
    *
    */
  object Spec2 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"$keyPoint $KleisliArrow is $functionComposition for $monad-s"

    object SomeFunctions {

      s"Some functions that take simple types and return higher-kinded types".p

      def str(x: Int): Option[String] = Some(x.toString)
      def toInt(x: String): Option[Int] = Some(x.toInt)
      def double(x: Int): Option[Double] = Some(x * 2)

      s"".p
      def opt(x: Int): Option[String] = Some(x.toString)
      def list(x: String) = List(x)
      def optToList[T](o: Option[T]) = o.toList
    }
    s"$bookmarks $ann_KleisliArrow2".p
    eg {
      /** [[Scalaz]] */

      import scalaz.Kleisli._
      import scalaz.std.option._

      import SomeFunctions._

      s"Lets compose those functions Ye Olde Way".p
      def oldSchool(i: Int) =
        for (x <- str(i);
             y <- toInt(x);
             z <- double(y))
          yield z

      s"And compose those functions $KleisliArrow way".p
      val funky = kleisli(str) >==> toInt >==> double

      oldSchool(1) must_== Some(2.0)
      funky(1)     must_== Some(2.0)
    }

    eg { /** [[Cats]] */

      //@todo
      success
    }

    eg {
      /** [[Scalaz]] */

      import scalaz.Kleisli._

      import SomeFunctions._

      s"now we can $operator_compose opt and list using optToList".p
      //@todo val multi = (kleisli(opt _) compose optToList) >=> list
      success
    }

    eg { /** [[Cats]] */

      //@todo
      success
    }

    object World {

      case class Continent(name: String, countries: List[Country] = List.empty)

      case class Country(name: String, cities: List[City] = List.empty)

      case class City(name: String, isCapital: Boolean = false, inhabitants: Int = 20)

      val Washington = City("Washington", isCapital = true, inhabitants = 9000000)
      val NewYork = City("New York", isCapital = false, inhabitants = 11000000)

      val NewDehli = City("New Dehli", isCapital = false, inhabitants = 20000000)
      val Calcutta = City("Calcutta", isCapital = false, inhabitants = 30000000)

      val data: List[Continent] = List(
        Continent("Europe"),
        Continent("America",
          List(
            Country("USA", List(Washington, NewYork))
          )
        ),
        Continent("Asia",
          List(Country("India", List(NewDehli, Calcutta))
          )
        )
      )

      def continents(name: String): List[Continent] =
        data.filter(k => k.name.contains(name))

      def countries(continent: Continent): List[Country] = continent.countries

      def cities(country: Country): List[City] = country.cities

      import scala.util.Try

      def save(cities: List[City]): Try[List[City]] =
        Try {
          var saved = List[City]()
          cities.foreach(c => saved = saved.::(c))
          saved
        }

      def inhabitants(c: City): Int = c.inhabitants
    }

    s"$keyPoint Examples of using the variations of the $operator_andThen,  " +
      s"either starting with a $KleisliArrow and following with functions of the form A => M[B] " +
      s"or following with adequate $KleisliArrow."

    s"$bookmarks $ann_KleisliArrow1".p
    eg {
      /** [[Scalaz]] */

      import scalaz.Kleisli._
      import scalaz.std.list._

      import World._

      s" Some operator have aliases: " +
        s" $$operator_>==> is alias for $operator_andThenK " +
        s" $$operator_>=>  is alias for $operator_andThen " +
        s" " +
        s" The same applies to $functionComposition with " +
        s" $$operator_<==< and $operator_composeK " +
        s" $$operator_<=<  and $operator_compose".p

      val allCities1 = kleisli(continents) >==>         countries  >==>         cities
      val allCities2 = kleisli(continents) >=>  kleisli(countries) >=>  kleisli(cities)
      val allCities3 = kleisli(cities)     <==<         countries <==<          continents
      val allCities4 = kleisli(cities)     <=<  kleisli(countries) <=<  kleisli(continents)

      allCities1("America") must_== allCities2("America")
      allCities1("Ameri")   must_== List(Washington, NewYork)
      allCities1("Asi")     must_== List(NewDehli, Calcutta)

      allCities2("America") must_== allCities3("America")
      allCities2("America") must_== allCities4("America")

      s" $$operator=<< takes a $monadicStructure compatible with the $KleisliFunction" +
        s"as its parameter and $operator_flatMap-s the function over this parameter.".p
      (allCities1 =<< List("Amer", "Asi")) must_== List(Washington, NewYork, NewDehli, Calcutta)

      s"With $operator_map we can map a function B => C over a $KleisliFunction of the structure A => M[B]".p
      val cityInhabitants = allCities1 map inhabitants
      cityInhabitants =<< List("Amer", "Asi") must_== List(9000000, 11000000, 20000000, 30000000)

      s"with $operator_mapK you can map a $KleisliFunction into another $monadicStructure, e.g. provide a function M[A] => N[B]".p
      val getAndSave = allCities1 mapK save
      //import scalaz.Success
      //@todo clarify getAndSave("America") must_== Success(allCities1("America").reverse)

      s"$operator_local can be used to prepend a $KleisliFunction of the form A => M[B] with a function of the form AA => A, " +
        s"resulting in a $KleisliFunction of the form AA => M[B]".p
      def index(i: Int) = data(i).name
      val allCitiesByIndex = allCities1 local index

      allCitiesByIndex(1) must_== List(Washington, NewYork)
    }

    eg {
      /** [[Cats]] */

      //@todo
      success
    }

    object Scalaz_FilesOp {

      import java.io.File
      import scala.io.Source
      import scalaz.Kleisli
      import scalaz.Kleisli._
      import scalaz.std.list._

      val files: String => List[File] = { (dir) =>
        new File(dir).listFiles().toList
      }

      def lengths: File => List[Int] = { (f) =>
          if (f.isDirectory)
            List(0)
          else
            Source.fromFile(f).getLines().toList.map(l => l.length())
        }

      /**
        * @todo better understand the point of [[ann_KleisliArrow4]]
        */
      s"$bookmarks $ann_KleisliArrow4, $ann_KleisliArrow5".p

      def lengthsK: Kleisli[List, File, Int] = kleisli((f: File) => {
        if (f.isDirectory)
          lengthsK =<< f.listFiles().toList
        else
          Source.fromFile(f).getLines().toList.map(l => l.length())
      })

        val lineLengths        =  kleisli(files) >==> lengths
        val etcLineLengths     = (kleisli(files) >==> lengths) <==< ((dir: String) => List("/etc/" + dir))
        val networkLineLengths = (kleisli(files) >==> lengths)  =<< List("network")
      }

      s"$keyPoint ..."
      s"$bookmarks $ann_KleisliArrow3".p
      eg {
        /** [[Scalaz]] */

        import scalaz.Kleisli._
        import scalaz.std.list._
        import Scalaz_FilesOp._

        import scala.util.Try

        /* @todo solve NullPointerException
        val dirs = kleisli(files) =<< List("/etc/network")
        println(dirs)

        val lengthsC = Try {
          networkLineLengths
        }
        println(lengthsC)
        interfacesLineLengths(0) must beGreaterThan 10
        */
        success
      }

      eg {
        /** [[Cats]] */

        //@todo
        success
      }
    }

  /**
    *
    */
  object Spec3 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

      object SomeFunctions {
        def a = (value: Int) => value * 2

        def b = (value: Int) => value + 1

        def ab = a.andThen(b)
      }

      s"$keyPoint $KleisliArrow composition is to $operator_flatMap as function composition is to $operator_map."

      s"$bookmarks $ann_KleisliArrow6".p
      eg {
        /** [[Scalaz]] */

        import scalaz.std.option._
        import scalaz.syntax.std.option._

        import SomeFunctions._

        s"Using $operator_map to achieve $functionComposition for regular functions.".p
        1.some.map(a).map(b)  must_== 3.some
        1.some.map(ab)        must_== 3.some

        import scalaz.Kleisli
        import scalaz.Kleisli._

        val ak = Kleisli( (value: Int) => a(value).some )
        val bk = Kleisli( (value: Int) => b(value).some )
        val abk = ak.andThen(bk)

        s"Using $operator_map and $operator_flatMap to achieve $functionComposition for $KleisliArrow-s.".p
        1.some.flatMap(ak.run).flatMap(bk.run)  must_== 3.some
        1.some.map(abk).get                     must_== 3.some
      }

      eg {
        /** [[Cats]] */

        import cats.instances.option._
        import cats.syntax.option._

        import SomeFunctions._

        s"Using $operator_map to achieve $functionComposition for regular functions.".p
        1.some.map(a).map(b)  must_== 3.some
        1.some.map(ab)        must_== 3.some

        import cats.data.Kleisli

        val ak = Kleisli( (value: Int) => a(value).some )
        val bk = Kleisli( (value: Int) => b(value).some )
        val abk = ak.andThen(bk)

        s"Using $operator_map and $operator_flatMap to achieve $functionComposition for $KleisliArrow-s.".p
        1.some.flatMap(ak.run).flatMap(bk.run)  must_== 3.some
        1.some.map(abk.run).get                 must_== 3.some
      }
  }

  /**
    *
    */
  object Spec4 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

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

    s"$keyPoint Today I’ll be exploring a few different ways in which you can compose programs. " +
      s"The examples that follow all deal with Vehicles - more specifically makes and parts:."

    s"$bookmarks $ann_FunctionComposition2".p
    eg {
      /** [[Scala]] */

      import SomeFunctions1._

      s"So we have a function Int =>  Make and then a function : Make => List[Part]. " +
        s"From set theory we know this implies we must have a function from Int => List[Part]. " +
        s"This is nothing more than simple $functionComposition:".p

      val f = parts compose make
      f(1) must_== List(part1, part2)

      s"Alternatively you can use $operator_andThen which works like $operator_compose, but with the arguments flipped:".p
      val g = make andThen parts
      g(1) must_== List(part1, part2)
    }

    s"$keyPoint Now we have a function make: Int => Option[Make] and a function parts: Make => Option[NonEmptyList[Part]]. " +
      s"Based on our first example we should have a way to create a function from Int to Option[NonEmptyList[Part]]. " +
      s"This isn’t immediately obvious however."

    s"$bookmarks $ann_FunctionComposition3"
    eg {
      /** [[Scalaz]] */

      import SomeFunctions2._

      s"While make does return a Make, it is wrapped inside an Option so we need to account for a possible failure. " +
        s"This leads to our first attempt:".p

      import scalaz.NonEmptyList

      val f: Option[Make] => Option[NonEmptyList[Part]] = {
        case Some(m) => parts(m)
        case _ => None
      }
      val g = f compose make
      g(1) must_== Some(NonEmptyList(part1, part2))
    }

    s"While this works, we had to manually create the plumbing between the two functions. You can imagine that with " +
      s"different return and input types, this plubming would have to be rewritten over and over. " +
      s"All the function f above is doing is serving as an adapter for parts. " +
      s"It turns out there is a couple of ways in which this pattern can be generalised."

    s"$monadicBind: " +
      s"Option is a $monad so we can define f using a $forComprehension:"

    eg {
      /** [[Scalaz]] */

      import scalaz.NonEmptyList
      import scalaz.syntax.bind._
      import scalaz.std.option._

      import SomeFunctions2._

      val f = (x: Int) => for {
        m <- make(x)
        p <- parts(m)
      } yield p

      f(1) must_== Some(NonEmptyList(part1, part2))

      s"Which is simply syntactic sugar for:".p

      val g = make(_:Int) flatMap (m => parts(m).map(p => p))
      g(1) must_== f(1)

      s"You can also use the symbolic alias for $operator_bind, which makes it a lot nicer".p

      val h = make(_:Int) >>= parts
      h(1) must_== f(1)
    }

    s"$keyPoint The reason this is better is that make and parts could operate under a different $monad but the client code would not need to change. " +
      s"In the example below, we’re operating under the List $monad:"

    eg {
      /** [[Scalaz]] */

      val words: (String) => List[String] = _.split( """\s""").toList
      val chars: String => List[Char] = _.toList

      val f = (phrase: String) => for {
        m <- words(phrase)
        p <- chars(m)
      } yield p

      val charList = List('M', 'o', 't', 'o', 'r', 'c', 'y', 'c', 'l', 'e', 's', 'a', 'r', 'e', 'f', 'u', 'n', 't', 'o', 'r', 'i', 'd', 'e', '!')
      f("Motorcycles are fun to ride!") must_== charList

      s"or even:".p
      val g = words(_: String) flatMap (w => chars(w).map(c => c))

      g("Motorcycles are fun to ride!") must_== charList
    }

    s"$keyPoint We used the exact same for comprehension syntax to compose these operations. This works because both Option and List are $monad-s."+
      s"Notwithstanding, this still feels like unnecessary plumbing. All we are doing with the $forComprehension / $operator_flatMap is " +
      s"extracting the values from their respective $monad-s to simply put them back in. It would be nice if we could simply do something " +
      s"like 'make compose parts' as we did in our first example."

    s"$KleisliArrow-s:" +
      s"A $KleisliArrow is simply a wrapper for a function of type A => F[B]. This is the same type of the second argument to the $monadicBind " +
      s"as defined in [[Scalaz]]:"

    s"$keyPoint By creating a $KleisliArrow from a function, we are given a function that knows how to extract the value " +
      s"from a $monad F and feed it into the underlying function, much like $operator_bind does, but without actually having " +
      s"to do any binding yourself."

    eg {
       /** [[Scalaz]] */

      import scalaz.NonEmptyList
      import scalaz.std.option._
      import scalaz.Kleisli
      import scalaz.Kleisli._

      import SomeFunctions2._

      s"To use a concrete example, let’s create a $KleisliArrow from our parts function:".p

      kleisli(parts)

      s"You can read this type as being a function which knows how to get a value of type Make from the Option $monad and will ultimately " +
        s"return an Option[NonEmptyList[Part]]. " +
        s"Now you might be asking, why would we want to wrap our functions in a $KleisliArrow?" +
        s"By doing so, you have access to a number of useful functions defined in the $Kleisli trait, one of which is <==< (aliased as $operator_composeK):".p

      val f1 = kleisli(parts) <==< make
      val f2 = kleisli(parts) composeK make

      s"This gives us the same result as the version using the $forComprehension but with less work and with code that looks similar to simple $functionComposition.".p

      f1(1) must_== Some(NonEmptyList(part1, part2))
      f2(1) must_== Some(NonEmptyList(part1, part2))
     }

    s"$keyPoint Not there yet" +
      s"One thing that was bugging me is the return type for parts above:" +
      s"Make => Option[NonEmptyList[Part]]"

    eg {
      /** [[Scalaz]] */

      import scalaz.std.option._
      import scalaz.Functor

      s"Sure this works but since lists already represent non-deterministic results, one can make the point that the Option type " +
        s"there is reduntant since, for this example, we can treat both None and the empty List as the absence of result. " +
        s"Let’s update the code:".p

      //@todo how to include code but not run it
//      val make  = (x: Int) => (x == 1).option(Make(1, "Suzuki"))
//
//      val parts: Make => List[Part] = {
//        case Make(1, _) => List(part1, part2)
//        case _ => Nil
//      }
      import SomeFunctions3._

      s"It seems we’re in worse shape now! As before, parts’s input type doesn’t line up with make’s return type. Not only that, " +
        s"they aren’t even in the same $monad anymore!" +
        s"This clearly breaks our previous approach using a $KleisliArrow to perform the composition. " +
        s"On the other hand it makes room for another approach: $functorLifting.".p

      s"Lifting" +
        s"In Scala - and category theory - $monad-s are $functor-s. As such both Option and List have access to a set of useful functor combinators. " +
        s"The one we’re interested in is called $operator_lift." +
        s"Say you have a function A => B and you have a functor F[A]. Lifting is the name of the operation that transforms the function A => B " +
        s"into a function of type F[A] => F[B]." +
        s"This sounds useful. Here are our function types again:".p

      //@todo how to include code but not run it
//      make: Int => Option[Make]
//      parts: Make => List[Part]

     s"We can’t get a function Int => List[Part] because make returns an Option[Make] meaning it can fail. " +
       s"We need to propagate this possibility in the composition. We can however lift parts into the Option monad, effectively changing " +
       s"its type from Make => List[Part] to Option[Make] => Option[List[Part]]:".p

      val f = Functor[Option].lift(parts) compose make
      f(1) must_== Some(List(part1, part2))

      s"f now has the type Int => Option[List[Part]] and we have once again successfully composed both functions without writing any plumbing code ourselves."+
        s"Mark pointed out to me that $operator_lift is pretty much the same as $operator_map but with the arguments reversed. " +
        s"So the example above can be more succintly expressed as:".p
      s"$bookmarks $ann_FunctionComposition4".p

      val g = make(_:Int).map(parts)
      g(1) must_== Some(List(part1, part2))
    }

    eg {
      /** [[Cats]] */

      import cats.instances.option._
      import cats.syntax.option._

      //@todo
      success
    }
  }

  /**
    *
    */
  object Spec6 extends org.specs2.mutable.Spec with AutoExamples with TextDsl {

    s"Some example".p

    eg {
      /**
        * [[bookmarks]] http://scalaz.github.io/scalaz/scalaz-2.9.1-6.0.4/doc.sxr/scalaz/example/ExampleKleisli.scala.html
        */
      import scalaz.std.option._
      import scalaz.Kleisli._

      val f = kleisli((n: Int) => if (n % 2 == 0) None else Some((n + 1).toString))
      val g = kleisli((s: String) => if (List(5, 7).exists(_ == s.length)) None else Some("[" + s + "]"))

      // Kleisli composition
      (List(7, 78, 98, 99, 100, 102, 998, 999, 10000) map (f >=> g apply _)) must_===
        List(Some("[8]"), None, None, Some("[100]"), None, None, None, Some("[1000]"), None)
    }
  }
}


