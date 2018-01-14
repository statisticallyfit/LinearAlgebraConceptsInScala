package org.specs2.guide.applicative

import org.fp._
import org.fp.studies._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Applicative_UserGuidePage extends UserGuidePage {

  def is = "Custom applicative".title ^ s2"""

### ${concepts.applicativeFunctor} mapping (of a function) preserves the type/shape of the ${concepts.applicativeFunctor}
  Examples for a 'custom' ${concepts.applicativeFunctor} with ${resources.Scalaz.md} and ${resources.Cats.md} in

 * ${link(applicative.operators.custom.Spec)}
 * ${link(applicative.operators.dfault.Spec)}

}

"""
}
