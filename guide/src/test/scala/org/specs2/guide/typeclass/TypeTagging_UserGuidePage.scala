package org.specs2.guide.typeclass

import org.fp._
import org.fp.studies._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object TypeTagging_UserGuidePage extends UserGuidePage {

  def is = "Tagging types".title ^ s2"""

  ${concepts.functor}s enable ${concepts.functorComposition} ${resources.Scala.md} and ${resources.Simulacrum.md} in

 * ${link(typeclass.tagging.Spec)}

"""
}
