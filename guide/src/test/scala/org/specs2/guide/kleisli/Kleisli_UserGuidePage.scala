package org.specs2.guide.kleisli

import org.fp._
import org.fp.studies._

//
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Kleisli_UserGuidePage extends UserGuidePage {

  def is = "Kleisli example".title ^ s2"""

## Examples for a ${concepts.KleisliArrow} ${concepts.functionComposition} with ${resources.Scalaz.md} and ${resources.Cats.md}

 * ${link(kleisli.composition.Spec1)}
 * ${link(kleisli.composition.Spec2)}
 * ${link(kleisli.composition.Spec3)}
 * ${link(kleisli.composition.Spec4)}
 * ${link(kleisli.composition.Spec5)}


"""
}
