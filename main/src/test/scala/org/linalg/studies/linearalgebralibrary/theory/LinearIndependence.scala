package org.linalg.studies.linearalgebralibrary.theory

/**
  * The vectorset that inherits this must tell whether its cols are linearly independent
  */
trait LinearIndependence[V, F <: Field[F]] {
     this: VectorSpace[V, F] =>

     def linearlyIndependent(): Boolean
}
