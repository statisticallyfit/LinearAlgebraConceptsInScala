package org.fp.studies.fp

import org.fp.bookmarks._
import org.fp.concepts._
import org.fp.resources._

//
import org.specs2.ScalaCheck
import org.specs2.common.CheckedSpec
import org.specs2.specification.Snippets
import org.specs2.execute.SnippetParams
import org.specs2.specification.dsl.mutable.TextDsl

/**
  * [[disjunction]] in [[Scalaz]]
  *
  */
object ValidationSpec extends org.specs2.mutable.Specification with Snippets with ScalaCheck with CheckedSpec with TextDsl {

  implicit def snippetParams[T]: SnippetParams[T] = defaultSnippetParameters[T].copy(evalCode = true).offsetIs(-4)

  override def is = s"$validation ".title ^ s2"""(continued from ${link(org.fp.studies.fp.DisjunctionSpec)})

`\/[A, B]` is also isomorphic to `Validation[A, B]`.

The subtle but important difference is that $applicativeFunctor instances for `Validation`
accumulates errors ("lefts") while $applyFunctor instances for `\/` "fail fast" on the first "left" they evaluate.

This fail-fast behavior allows `\/` to have lawful $monad instances that are consistent with their $applicativeFunctor instances, while `Validation` cannot.

    $bookmarks: ${ann_ScalazValidation1.md}
    ${snippet{

    import scalaz.{\/, -\/, \/-, ValidationNel, NonEmptyList}
    import java.time.LocalDate

    case class Musician(name: String, born: LocalDate)

    object Errors {
      val NAME_EMPTY = "name: cannot be empty."
      val AGE_TOO_YOUNG = "age: too young."
    }

    def validate(musician: Musician): ValidationNel[String, Musician] = {
      import Errors._
      import scalaz.Scalaz._

      def validName(name: String): ValidationNel[String, String] =
        if (name.isEmpty) NAME_EMPTY.failureNel
        else name.success

      def validateAge(born: LocalDate): ValidationNel[String, LocalDate] =
        if (born.isAfter(LocalDate.now().minusYears(12))) AGE_TOO_YOUNG.failureNel
        else born.success

      s"$validation in an $applicativeFunctor: many can be composed or chained together".p
      s"$bookmarks: ${ann_ScalazValidation2.md}".p
      (validName(musician.name) |@| validateAge(musician.born)) ((_, _) => musician)
    }

    val either: \/[String, Musician] = \/-(Musician("", LocalDate.now().minusYears(11)))

    val r = either match {
      case \/-(value) => either.@\?/(_ => validate(value))
      case _ => \/-(Musician("", LocalDate.now()))
    }

    s"If any failure in the chain, failure wins: All errors get appended together".p
    check(r must_== -\/(NonEmptyList(Errors.NAME_EMPTY, Errors.AGE_TOO_YOUNG)))
  }}

### A more complex example

  ${snippet {
    s"$bookmarks: ${ann_ScalazValidation3.md}".p

    import scalaz.std.tuple._
    import scalaz.std.list._
    import scalaz.syntax.apply._
    import scalaz.syntax.foldable._
    import scalaz.{Validation, Success, Failure, ValidationNel, NonEmptyList}
    import scalaz.Validation.FlatMap._
    import scalaz.syntax.validation._
    import scalaz.syntax.nel._

    case class ScoreRange(min: Int, max: Int)
    case class Entity(id: Long, name: String, scoreRange: ScoreRange)

    object System {

      def loadCsv(): List[String] = {
        List("1,me,3,4", "2,he,0,100", "No3,aaaaaaaaaaaaaaaa,1,10")
      }

      def batchUpdate(entities: List[Entity]) = println("Updating = " + entities)

      def outputErrors(errors: List[String]) = println("Errors = " + errors)
    }

    object Validator {

      def validate(records: List[String]): ValidationNel[String, List[Entity]] = {
        records.foldMap { record =>
          for {
            columns <- validateColumn(record)
            entity <- validateEntity(columns)
          } yield List(entity)
        }
      }

      def validate2(record: String): ValidationNel[ErrorInfo, Entity] = {
        (for {
          columns <- validateColumn(record)
          entity <- validateEntity(columns)
        } yield entity) leftMap { e =>
          (e.toList, record).wrapNel
        }
      }

      def validateColumn(record: String): ValidationNel[String, Array[String]] = {
        val columns = record.split(",")
        if (columns.size == 4) columns.successNel
        else "less columns".failureNel
      }

      def validateEntity(col: Array[String]): ValidationNel[String, Entity] = {
        (validateId(col(0)) |@|
          validateName(col(1)) |@|
          validateScoreRange(col(2), col(3))) (Entity)
      }

      def validateId(id: String): ValidationNel[String, Long] = {
        Validation.fromTryCatchNonFatal(id.toLong).leftMap(_ => NonEmptyList("invalid id"))
      }

      def validateName(name: String): ValidationNel[String, String] = {
        if (name.size <= 10) name.successNel
        else "name too long".failureNel
      }

      def validateScoreNum(num: String, column: String): ValidationNel[String, Int] = {
        Validation.fromTryCatchNonFatal(num.toInt).leftMap(_ => NonEmptyList(s"invalid $column"))
      }

      def validateMinMax(min: String, max: String): ValidationNel[String, (Int, Int)] = {
        (validateScoreNum(min, "min") |@| validateScoreNum(max, "max")) ((x, y) => (x, y))
      }

      def validateScoreRangeConstraint(min: Int, max: Int): ValidationNel[String, ScoreRange] = {
        if (min <= max) ScoreRange(min, max).successNel
        else "min is grater than max".failureNel
      }

      def validateScoreRange(min: String, max: String): ValidationNel[String, ScoreRange] = {
        validateMinMax(min, max).flatMap { case (n, x) => validateScoreRangeConstraint(n, x) }
      }
    }

    val records: List[String] = System.loadCsv()

    val validated: ValidationNel[String, List[Entity]] = Validator.validate(records)

    println("== validated1 ==")
    validated must_== Failure(NonEmptyList("invalid id", "name too long"))

    validated match {
      case Success(entities) => System.batchUpdate(entities)
      case Failure(errors) => System.outputErrors(errors.toList)
    }

    type ErrorInfo = (List[String], String)

    val validated2: List[ValidationNel[ErrorInfo, Entity]] = records.map(Validator.validate2)

    val results2: (List[ErrorInfo], List[Entity]) = validated2.foldMap {
      case Success(s) => (Nil, List(s))
      case Failure(f) => (f.toList, Nil)
    }
    println("== validated2 ==")
    System.outputErrors(results2._1.head._1)
    System.batchUpdate(results2._2)
  }}

### Another example

  ${snippet {

    import scalaz.std.list._
    import scalaz.std.tuple._
    import scalaz.syntax.foldable._

    case class Product(name: String)

    case class Item(name: String)

    val products: List[Product] = List(
      Product("foo"),
      Product("bar")
    )

    def createItem(product: Product): Item = Item(product.name)

    def createCodes(name: String, item: Item): List[String] = List(name)

    //@todo https://gist.github.com/tonymorris/4366536
    val (allItems, allCodes) = products.foldMap { p =>
      val item = createItem(p)
      (List(item), createCodes(p.name, item))
    }

    allItems must_== List(Item("foo"), Item("bar"))
    allCodes must_== List("foo", "bar")
  }}
  """.stripMargin

  /**
    * @todo http://codegists.com/snippet/scala/scalaz-validationscala_animatedlew_scala
    * @todo http://codegists.com/snippet/scala/scalaz-nelscala_manjuraj_scala
    * @todo http://codegists.com/snippet/scala/scalaz-examplesscala_rodoherty1_scala
    * @todo http://codegists.com/snippet/scala/scalaz-monad-transformersscala_matterche_scala
    * @todo http://codegists.com/snippet/scala/scalaz-streams-task-esscala_taisukeoe_scala
    */

}