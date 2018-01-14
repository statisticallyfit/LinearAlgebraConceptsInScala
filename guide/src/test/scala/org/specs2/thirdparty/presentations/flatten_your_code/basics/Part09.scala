package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
//
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Part09 extends UserGuidePage  {

  def is = s"Flatten your code : basics, part 9".title ^ s2"""

Most well-known $typeClass-es in Play are the ones for JSON: Reads, Writes and Format Play provides some `instances` of them:
`Format[String`], `Reads[DateTime]`, etc.

You can define `instances` of these type classes for your own classes.

### Exercise

Without looking at the previous part, create a type class `Serializable`, a function `toBytes` that implicitly uses this
$typeClass, and instances for `Int` and `String`.

${snippet{
/**/
    trait Serializable[A] {
      def serialize(value: A): Array[Byte]
    }

    def toBytes[A: Serializable](value: A) = implicitly[Serializable[A]].serialize(value)

    implicit val StringSerializable = new Serializable[String] {
      override def serialize(value: String) = value.toString.getBytes
    }

    implicit val IntSerializable = new Serializable[Int] {
      override def serialize(value: Int) = value.toString.getBytes
    }
  }}

Prev ${link(Part08)}

Next ${link(Part10)}
  """
}
