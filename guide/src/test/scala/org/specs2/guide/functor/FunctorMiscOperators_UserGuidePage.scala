package org.specs2.guide.functor

import org.fp._
import org.fp.studies._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object FunctorMiscOperators_UserGuidePage extends UserGuidePage {

  def is = "Functor misc operators".title ^ s2"""

A ${concepts.functor} has other operators besides ${concepts.operator_map}: ${concepts.operator_void}, ${concepts.operator_Fproduct}, ${concepts.operator_as}
  See examples with ${resources.Scalaz.md} and ${resources.Cats.md}

 * ${link(functor.operators.misc.Spec)}

"""
}
