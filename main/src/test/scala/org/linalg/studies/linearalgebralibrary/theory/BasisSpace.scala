package org.linalg.studies.linearalgebralibrary.theory


import org.linalg.studies.linearalgebralibrary.number._

//note M ~ for matrix
trait BasisSpace[M, F <: Field[F]] {

     this: VectorSpace[M, F] with LinearIndependence[M, F] =>

     //checks whether this space/set is basis of vector space of Real/Rational/orComplex just by
     // checking its dimension
     // example: this vspace/set is basis of R^4 if the basis has 4 elements in each vector && vectors
     // are linindep.
     //note Any n independent vecs in R^n are basis for R^n, and also as basis for R^n has n independent
     // vectors in R^n
     def isBasisOfSpaceWith(dim: Int): Boolean
     def basis(): M
}

trait Basis[S, N <: Number[N]] /*extends Orthonormal[V] with Span[Basis[V, F], F]*/ {

     this: VectorSpace[S, N] with Span[S, N] with LinearIndependence[S, N] =>

     //note ifvecset cols are linearly independent, then the vecset is a basis for vecpsace V^n,
     // if not return None.
     // which means this vecset is not a basis for the V^n vecspace. prereq is isBasisOfSpaceWith function
     def basis(): Option[S]
     def isBasisOfSpaceWith(dim: Int): Boolean
}
