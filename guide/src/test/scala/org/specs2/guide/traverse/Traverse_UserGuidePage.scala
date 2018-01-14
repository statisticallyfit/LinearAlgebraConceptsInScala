package org.specs2.guide.traverse

import org.fp._
import org.fp.studies._
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Traverse_UserGuidePage extends UserGuidePage {

  def is = "Traverse functors".title ^ s2"""

  ${concepts.traverseFunctor} ...
  Examples for a 'custom' ${concepts.traverseFunctor} with ${resources.Scalaz.md} and ${resources.Cats.md} in

 * ${link(traverse.operators.custom.CustomTraverseSpec)} and ${link(traverse.operators.dfault.DefaultTraverseSpec)}

}

"""
}
