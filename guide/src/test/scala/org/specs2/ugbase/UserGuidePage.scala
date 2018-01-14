package org.specs2.ugbase


import org.specs2._
import org.specs2.execute._
import org.specs2.specification.core.Fragments
import org.specs2.specification.{Snippets, Forms}
import org.specs2.form.Card
import org.specs2.common.CheckedSpec
//

/**
 * base class for creating specs2 user guide pages.
 */
abstract class UserGuidePage extends Specification with UserGuideVariables with Forms
  with Snippets with ScalaCheck with CheckedSpec {

  implicit def snippetParams[T]: SnippetParams[T] = defaultSnippetParameters[T].copy(evalCode = true)

  override def map(fs: =>Fragments) = super.map(fs.compact)
}

abstract class UserGuideCard extends Card with UserGuideVariables with Forms

