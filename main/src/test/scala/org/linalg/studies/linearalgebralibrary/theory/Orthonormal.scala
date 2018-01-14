package org.linalg.studies.linearalgebralibrary.theory

/**
  *
  */

//todo only problem is that we can't constrain V to be vecspace because if we do, then banacspace extends normal
// won't work
//note: the trick to putting self types: for trait A, its self type this: B is such that B is higher than A and such
// note -- A is a type of B or is related.
// example: anything that implements Normal[B, F] must also implement vecspace and banachspace because these two use
// normal.



trait Normal[B/* <: VectorSpace[V, N]*/, F <: Field[F]] {
     this: BanachSpace[B, F] =>

     def norm(): F
     def normalize(): B
     def isNormalized(): Boolean
}

trait OrthogonalTo[V, F <: Field[F]] {
     this: VectorSpace[V, F] =>

     def isOrthogonalTo(that: V): Boolean
}

trait OrthogonalIdentity[V, F <: Field[F]] {
     this: VectorSpace[V, F] =>

     def isOrthogonal(): Boolean
}

trait Orthogonal[V, F <: Field[F]] extends OrthogonalTo[V, F] with OrthogonalIdentity[V, F] {
     this: VectorSpace[V, F] =>

     def orthogonalize(): V
}

trait Orthonormal[B <: BanachSpace[B, F], F <: Field[F]] extends Orthogonal[B, F] with Normal[B, F] {
     this: VectorSpace[B, F] with BanachSpace[B, F]  =>

     def orthonormalize(): B = orthogonalize().normalize()
}

