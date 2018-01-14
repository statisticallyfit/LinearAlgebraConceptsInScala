package org.specs2.guide.typeclass

import org.specs2.ScalaCheck
import org.specs2.common.CheckedSpec
import org.specs2.ugbase.UserGuidePage

//
import org.specs2.specification.Snippets
import shapeless.test.illTyped

import scala.reflect.runtime.universe._

//trait Includes {
//
//  type defs = Defs
//  val defsInclude: String = showRaw(reify {
//
object Defs {
  trait A1
  trait A2 extends A1
  trait B1
  trait B2 extends B1

  def f(a : A1) : B1 = new B1{}
  def g(a : A2) : B2 = new B2{}

  val fv1 : Function1[A1, B1] = f
  var fv2 : (A1) => B1 = f

  val gv1 : Function1[A2, B2] = g
  var gv2 : (A2) => B2 = g
}
//    defs = new Defs{}
//
//}.tree)
//}

/**
  *
  */
object Variance_UserGuidePage extends UserGuidePage /*with Snippets with ScalaCheck with CheckedSpec*/ {

  def is = s"Variance example".title ^ s2"""

### Functions are types

  ${snippet{

    import Defs._

    illTyped("""
      gv2 = fv1
      """)
    illTyped("""
      fv2 = gv1
      """)

    val obj = f _
    val mirror = runtimeMirror(obj.getClass.getClassLoader)
    val insMirror = mirror reflect obj
    val originType = insMirror.symbol.toTypeConstructor

    println(originType.toString)
    check(typeOf[(A1) => B1].typeConstructor =:= originType.typeConstructor must_== true)
    check(typeOf[A1].typeConstructor <:< typeOf[A2].typeConstructor must_== true)
  }}

  And this works nice.
  """
}
