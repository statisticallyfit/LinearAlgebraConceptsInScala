package org.fp.studies.typeclass.definition.manual

import org.fp.concepts._
import org.fp.resources._
import org.fp.studies.typeclass.definition.auto

//
import org.specs2.common.SnippetHelper._
import org.specs2.specification.Snippets


/**
  * @see [[org.fp.resources.Simulacrum]]
  */
object SimulacrumSpec extends org.specs2.Specification with Snippets {

  def is = s"Defining $typeClass-es automatically".title ^ s2"""

### The conventional steps of defining a modular $typeClass in Scala used to look like:

 * Define $typeClass contract trait Foo.
 * Define a $companionObject Foo with a helper method apply that acts like implicitly, and a way of defining Foo instances typically from a function.
 * Define FooOps class that defines unary or binary operators.
 * Define FooSyntax trait that implicitly provides FooOps from a Foo instance.

Frankly, these steps are mostly copy-paste boilerplate except for the first one. Enter Michael Pilquist (@mpilquist)’s simulacrum.
${Simulacrum.md} magically generates most of steps 2-4 just by putting `@typeclass` annotation. By chance, Stew O’Connor (@stewoconnor/@stew)’s #294 got merged,
which refactors ${Cats.md} to use it.

 ${incl[auto.Definitions]}

 And now usages to exemplify

 ${incl[auto.Usages]}
      """
}

