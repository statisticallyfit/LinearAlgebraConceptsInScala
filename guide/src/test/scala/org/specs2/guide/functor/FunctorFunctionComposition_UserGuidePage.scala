package org.specs2.guide.functor

import org.fp._
import org.fp.studies._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object FunctorFunctionComposition_UserGuidePage extends UserGuidePage {

  def is = s"Functor function composition".title ^ s2"""

### ${concepts.typeClass}s can be defined manually

 The default ${resources.Scala.md} way, or automated using ${resources.Simulacrum.md} with ${resources.Scalaz.md} and ${resources.Cats.md}:

 * ${link(functor.composition.Spec)}

"""
}
