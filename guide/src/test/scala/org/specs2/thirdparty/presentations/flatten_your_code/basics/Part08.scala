package org.specs2.thirdparty.presentations.flatten_your_code.basics

import org.fp.concepts._
//
import org.specs2.ugbase.UserGuidePage

/**
  *
  */
object Part08 extends UserGuidePage  {

  def is = s"Flatten your code : basics, part 8".title ^ s2"""

Now we go off the path a bit, and take a scenic route along $typeClass-es.

We'll look at various way to do polymorphism. Our example is that we want to serialize objects to bytes.

Subtype polymorphism: all types that must be serialized extend a common trait.

${snippet{
/**/
    object SubtypePolymorphism {

      trait Serializable {
        def bytes: Array[Byte]
      }

      def toBytes(value: Serializable) = value.bytes
    }
  }}

Often impractical, because all classes must extend `Serializable`. What if we want to serialize `String` or `Int`???

An alternative is $ad_hocPolymorphism, also known as $functionOverloading:

${snippet{
/**/
    object AdHocPolymorphism {
      def toBytes(value: String): Array[Byte] = value.getBytes
      def toBytes(value: Int): Array[Byte] = value.toString.getBytes
    }
  }}

Also impractical, because now our serialization library must know about all possible types we want to serialize.
What about the custom types in our app?

Solution: $glueObject-s: an object that knows how to serialize a single type.
We can create these for all types we want to serialize, without needing to change those types.
Also, the library that accepts objects-that-can-be-serialized doesn't need to know about all these types in advance

${snippet{
/**/
    object GlueObjects {
      trait Serializable[A] {
        def serialize(value: A): Array[Byte]
      }

      def toBytes[A](value: A, serializer: Serializable[A]): Array[Byte] = serializer.serialize(value)

      val StringSerializable = new Serializable[String] {
        override def serialize(value: String) = value.getBytes
      }

      val IntSerializable = new Serializable[Int] {
        override def serialize(value: Int) = value.toString.getBytes
      }

      // Using this:
      val myString = "Bananaphone!"
      val myStringBytes = toBytes(myString, StringSerializable)
    }

  }}

This works fine, but it's often a bit awkward to use with these glue objects.

In scala, this can be made nicer by making the glue object implicit:

${snippet{
/**/
    object GlueObjects2 {
      trait Serializable[A] {
        def serialize(value: A): Array[Byte]
      }

      def toBytes[A](value: A)(implicit serializer: Serializable[A]) = serializer.serialize(value)

      // Or using a `Context Bound`, which is syntactic sugar for the one above
      def toBytes2[A : Serializable](value: A) = implicitly[Serializable[A]].serialize(value)

      implicit val StringSerializable = new Serializable[String] {
        override def serialize(value: String) = value.getBytes
      }

      implicit val IntSerializable = new Serializable[Int] {
        override def serialize(value: Int) = value.toString.getBytes
      }

      // Using this:
      val myString = "Bananaphone!"
      val myStringBytes = toBytes(myString)
    }
  }}

This eliminates syntactic overhead.

This pattern is called $typeClass-es. Our trait `Serializable[A]` is called the $typeClass.
The implemented $glueObject `StringSerializable` and `IntSerializable` are called $typeClassInstance-es'.

Prev ${link(Part07)}

Next ${link(Part09)}
  """
}
