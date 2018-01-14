package org.linalg.studies.linearalgebralibrary.theory

/**
  *
  */
trait Dimension[V, F <: Field[F]]{
     this: VectorSpace[V, F] =>

     def dimension(): Int
}


