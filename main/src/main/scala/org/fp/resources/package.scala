package org.fp

import org.fp.resources.intf.ResourceType.{book, blog, framework, ResourceTypeVal}

import scala.reflect.ClassTag

/**
  *
  */
package object resources {

  object intf {

    object ResourceType {

      sealed trait ResourceTypeVal

      trait blog extends ResourceTypeVal
      trait book extends ResourceTypeVal
      trait framework extends ResourceTypeVal
      trait project extends ResourceTypeVal

      //val resourceTypes = Seq(blog, book, framework, project)
    }

    trait Resource[R <: ResourceTypeVal] {

      def id : String
      //def kind: R
      def url: String
    }

    trait Annotation[R <: ResourceTypeVal] {

      def resource: Resource[R]
      def reference: String
    }
  }

  case class Resource[R <: ResourceTypeVal](id: String, url: String)(implicit m: ClassTag[R]) extends intf.Resource[R] {

    override def toString : String = {
      //@todo s"[${m.runtimeClass.getSimpleName} $id]"
      s"(resource)$id"
    }
    def is = toString
    def md = s"[$id]($url)"

  }
  case class Annotation[R <: ResourceTypeVal](resource: Resource[R], reference: String)(implicit m: ClassTag[R]) extends intf.Annotation[R] {
    override def toString : String = {
      //@todo s"[${m.runtimeClass.getSimpleName} annotation ${resource.is}]"
      s"annotation of ${resource.is}"
    }
    def is = toString
    def md = s"`$is`"
  }

  /**
    * Resources specific to this project about "functional programming in Scala"
    */
  val Scala = Resource[framework]("Scala", "https://github.com/scala/scala")
  val Scalaz = Resource[framework]("Scalaz", "https://github.com/scalaz/scalaz")
  val Cats = Resource[framework]("Cats", "https://github.com/typelevel/cats")
  val Simulacrum = Resource[framework]("Simulacrum", "https://github.com/mpilquist/simulacrum")
  val ScalaCheck = Resource[framework]("ScalaCheck", "https://www.scalacheck.org/")
  val Specs2 = Resource[framework]("Specs2", "https://etorreborre.github.io/specs2/")
  val Discipline = Resource[framework]("Discipline", "https://github.com/typelevel/discipline")
  val KindProjector = Resource[framework]("non/kind-projector", "https://github.com/non/kind-projector")


  val learningScalaz = Resource[blog]("Learning Scalaz", "http://eed3si9n.com/learning-scalaz/")
  val learningScalazFromLearningScalaz = Resource[blog]("Learning Scalaz from Learning Scalaz", "https://earldouglas.com/notes/learning-scalaz.html")
  val herdingCats = Resource[blog]("Herding Cats", "http://eed3si9n.com/herding-cats/")

  val timperrett = Resource[blog]("timperrett blog", "http://timperrett.com/")

  val scalaWithCats = Resource[book]("Advanced scala with Cats", "[Noel Welsh, Dave Gurnell] Advanced scala with Cats.pdf")

  val stackOverflow = Resource[blog]("Stack overflow", "http://stackoverflow.com/")

  val casualMiracles = Resource[blog]("Casual Miracles", "http://www.casualmiracles.com")

  val cakeSolutions = Resource[blog]("Cake Solutions", "http://www.cakesolutions.net/")

  val misc = Resource[blog]("Misc sources", "")
}

