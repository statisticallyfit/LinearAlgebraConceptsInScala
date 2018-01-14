package org.specs2.guide.arrow

import org.fp._
import org.fp.studies._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Arrow_UserGuidePage extends UserGuidePage {

  def is = "Arrow example".title ^ s2"""

### ${concepts.Arrow} .... Examples for a ${concepts.Arrow} ${concepts.functionComposition} with ${resources.Scalaz.md} and ${resources.Cats.md} in
 * ${link(arrow.operators.dfault.Spec1)}

}

"""
}
