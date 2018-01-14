package org.linalg.studies.linearalgebralibrary.theory

import org.linalg.studies.linearalgebralibrary.number._
/**
  *
  */
// a vector set that spans a vector space has this trait
trait Span[V/* <: VectorSet[N]*/, N <: Number[N]]{

     this: VectorSpace[V, N] /*with Rank[this, F] */=>

     //checks whether this vectorset spans the vector space of Real/Rational/orComplex just by checking its dimension
     //example: this vspace/set is span of R^4 if the basis has 4 elements in each vector
     def spansSpaceWith(dim: Int): Boolean
     // checks whether "that" vector is in the span of our vectorset/space. Like prerequisite to getspanningcoefs
     def isSpanned(vset: V): Boolean
     //gets coefficients that make linear combination
     def getSpanningCoefficients(vset: V): V
     //gets a vector in the space whose coordinate vector relative to `this` vecset is `coefs`
     def getVectorInSpace(coefs: Vector[N]): Vector[N]
}



//a vector space that is spanned by a vectorset has this trait
/*trait Spanned[S : VectorSpace[S, N] , N <: Number[N]]{

     //type Space <: VectorSpace[Space, N]

     //todo how ot make vector extend vectorset? (not possible...)

     //checks whether this vecspace is spanned by a vecset just by checking its dimension (of vecspace)
     def isSpannedBy(vset: VectorSet[N]): Boolean // = space.dimension() == setOfVectors.numCols
     def isSpannedBy(v: Vector[N]): Boolean // = space.dimension() == setOfVectors.numCols

     // checks whether "that" vector is in the span of our vecspace. Like prerequisite to getspanningcoefs
     def isSpanned(vset: VectorSet[N]): Boolean
     def isSpanned(b: Vector[N]): Boolean // todo how to implement?

     //gets coefficients that make linear combination
     // todo check solve when system is underdetermined
     def getSpanningCoefficients(b: Vector[N]): Vector[N] //= setOfVectors.solve(b)
     def getSpanningCoefficients(vset: VectorSet[N]): VectorSet[N]
}*/






/*class Span[N <: Number[N]](setOfVectors: VectorSet[N]){

     //checks whether this vectorset spans a vector space just by checking its dimension
     def spans[V <: VectorSpace[V]](space: V): Boolean = space.dimension() == setOfVectors.numCols

     // checks whether "that" vector is in the span of our vectorset/space. Like prerequisite to getspanningcoefs
     def isSpanned(b: Vector[N]): Boolean // todo how to implement?

     //gets coefficients that make linear combination
     def getSpanningCoefficients(b: Vector[N]): Vector[N] = setOfVectors.solve(b) //todo check solve when
     // underdetermined
}*/
