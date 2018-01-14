package org.linalg.studies.linearalgebralibrary.theory


import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.util.Implicits._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
import org.linalg.studies.linearalgebralibrary.vector._

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

//------------------------------------------------------------------------------------------------------


/**
  * A Banach space, B, is a complete normed vector space such that every Cauchy sequence (with respect
  * to the metric d(x, y) = |x - y|) in B has a limit in B.
  */
// TODO fix how to make F extend Field and comparable!!!??
//todo should this extend Normal interface?
trait BanachSpace[B, F <: Field[F] /*& Comparable<F>*/] extends VectorSpace[B, F] with Normal[B, F] {

     // |⋅| : B → F
     //norm assigns a strictly positive length or size to all vectors in the vector space, other than the zero vector.
     def norm(): F //note calculates the 2-norm
     def normalize(): B
     def isNormalized(): Boolean
}



//------------------------------------------------------------------------------------------------------

/**
  * A Hilbert space is an inner product space, an abstract vector space in which distances and angles
  * can be measured. It is also "complete", meaning that if a sequence of vectors is Cauchy, then it
  * converges to some limit in the space.
  */
trait HilbertSpace[H, F <: Field[F] /*& Comparable[F]*/] extends BanachSpace[H, F] {

     //∠ : H × H → F
     // Inner product formalizes the geometrical notions such as the length of a vector and the angle between two vectors.
     def angle(that: H): Angle

     // <⋅,⋅> : H × H → F
     // Inner product formalizes the geometrical notions such as the length of a vector and the angle between two vectors.
     def dotProduct(that: H): F
}


//------------------------------------------------------------------------------------------------------

/**
  * A vector space is a set V together with two binary operations that combine
  * two entities to yield a third, called vector addition and scalar multiplication.
  * Vector spaces fall into two categories: A vector space V is said to be ﬁnite-dimensional if there is a finite set
  * of vectors in V that spans V and is said to be inﬁnite-dimensional if no such set exists.
  */
trait VectorSpace[V, F <: Field[F]] extends AbelianGroup[V] with Dimension[V, F] {

     this: AbelianGroup[V] with Dimension[V, F] =>
     //note must logically also say 'with basisvecspace' but not practically possible for row/colnull spaces
     //note inherited from abelian group: +,-,opposite, zero
     //note inherited from dimension: dimension()

     // The result of applying this function to a scalar, c, in F and v in V is denoted cv.
     def scale(constant: F): V
     def scale(constant: Double): V
}

//------------------------------------------------------------------------------------------------------


// TODO
/*trait LinearSubspace extends VectorSpace[LinearSubspace]
trait Subspace extends VectorSpace[Subspace]  //def isSubspaceOf(another: )*/


class RowSpace[N <: Number[N]: TypeTag](vset: VectorSet[N])
     extends AbelianGroup[RowSpace[N]] //with Ring[RowSpace[N]] with Monoid[RowSpace[N]]
          with VectorSpace[RowSpace[N], N] with BanachSpace[RowSpace[N], N]
          with LinearIndependence[RowSpace[N], N]
          with BasisSpace[RowSpace[N], N] with Dimension[RowSpace[N], N]
          with Orthonormal[RowSpace[N], N] {

     private val rowBasis: Matrix[N] = makeBasisMatrix()

     def getVectorSet(): VectorSet[N] = vset
     def getMatrix(): Matrix[N] = vset //note implicit here

     def tempGetBasis() = rowBasis
     def isInRowSpace(b: Vector[N]): Boolean = rowBasis.isSpanned(new VectorSet(b))
     def getRowSpaceVector(coefs: Vector[N]): Vector[N] = rowBasis.getVectorInSpace(coefs)
     def dimension(): Int = rowBasis.numRows
     def isBasisOfSpaceWith(dim: Int): Boolean = rowBasis.spansSpaceWith(dim) && rowBasis.linearlyIndependent()
     def basis(): RowSpace[N] = new RowSpace(makeBasisMatrix())
     private def makeBasisMatrix(): Matrix[N] = getMatrix().reducedRowEchelon().transpose()
     def linearlyIndependent(): Boolean = getMatrix().linearlyIndependent() //note: is rowspace cols linindep?

     def norm(): N = rowBasis.norm()
     def normalize(): RowSpace[N] = new RowSpace(rowBasis.normalize())
     def isNormalized(): Boolean = rowBasis.isNormalized()
     def orthogonalize(): RowSpace[N] = new RowSpace(rowBasis.orthogonalize())
     def isOrthogonal(): Boolean = rowBasis.isOrthogonal()
     def isOrthogonalTo(that: RowSpace[N]): Boolean = rowBasis.isOrthogonalTo(that.getMatrix())

     def ZERO(): RowSpace[N] = new RowSpace(getVectorSet().ZERO())
     def +(that: RowSpace[N]): RowSpace[N] = new RowSpace(getVectorSet() + that.getVectorSet()) //todo correct?
     def -(that: RowSpace[N]): RowSpace[N] = new RowSpace(getVectorSet() - that.getVectorSet()) //todo correct?
     def opposite(): RowSpace[N] = new RowSpace(getVectorSet().opposite())
     def scale(factor: N): RowSpace[N] = new RowSpace(getVectorSet().scale(factor))
     def scale(factor: Double): RowSpace[N] = new RowSpace(getVectorSet().scale(factor))

     override def toString: String = getVectorSet().toString
}



class ColumnSpace[N <: Number[N]: TypeTag](vset: VectorSet[N])
     extends AbelianGroup[ColumnSpace[N]] //with Ring[ColumnSpace[N]] with Monoid[ColumnSpace[N]]
          with VectorSpace[ColumnSpace[N], N] with BanachSpace[ColumnSpace[N], N]
          with LinearIndependence[ColumnSpace[N], N]
          with BasisSpace[ColumnSpace[N], N] with Dimension[ColumnSpace[N], N]
          with Orthonormal[ColumnSpace[N], N] {


     private val colBasis: Matrix[N] = makeBasisMatrix()

     def getVectorSet(): VectorSet[N] = vset
     def getMatrix(): Matrix[N] = vset

     def isInColSpace(b: Vector[N]): Boolean = colBasis.isSpanned(new VectorSet(b))
     def getColumnSpaceVector(coefs: Vector[N]): Vector[N] = colBasis.getVectorInSpace(coefs)
     def dimension(): Int = colBasis.numCols
     def linearlyIndependent(): Boolean = getMatrix().linearlyIndependent() //note: is rowspace cols linindep?
     def isBasisOfSpaceWith(dim: Int): Boolean = colBasis.spansSpaceWith(dim) && colBasis.linearlyIndependent()
     //todo is this also called the image basis? matrixLib seems to return rows for rref pivot cols, not cols for
     // rref pivot cols (so matrixLib seems incorrect)
     // See:
     // http://math.stackexchange.com/questions/1286677/finding-basis-for-image-of-linear-transformation
     // note: best: https://crazyproject.wordpress.com/2011/07/17/find-bases-for-the-image-and-kernel-of-a-given-linear-transformation-2/
     // http://math.stackexchange.com/questions/895406/how-to-find-a-basis-of-an-image-of-a-linear-transformation
     def basis(): ColumnSpace[N] = new ColumnSpace(colBasis)
     private def makeBasisMatrix(): Matrix[N]={
          val rref: Matrix[N] = getMatrix().reducedRowEchelon()
          val freeColIndices: Array[Int] = Util.GenOps.getIndicesOfFreeColumns(rref)
          val colBasisIndices: Array[Int] =
               (0 until rref.numCols).filterNot(i => freeColIndices.contains(i)).toArray
          Matrix(colBasisIndices.map(c => getMatrix().getCol(c)):_*)
     }

     def norm(): N = colBasis.norm()
     def normalize(): ColumnSpace[N] = new ColumnSpace(colBasis.normalize())
     def isNormalized(): Boolean = colBasis.isNormalized()
     def orthogonalize(): ColumnSpace[N] = new ColumnSpace(colBasis.orthogonalize())
     def isOrthogonal(): Boolean = colBasis.isOrthogonal()
     def isOrthogonalTo(that: ColumnSpace[N]): Boolean = colBasis.isOrthogonalTo(that.getMatrix())

     def ZERO(): ColumnSpace[N] = new ColumnSpace(getVectorSet().ZERO())
     def +(that: ColumnSpace[N]): ColumnSpace[N] = new ColumnSpace(getVectorSet() + that.getVectorSet()) //todo correct?
     def -(that: ColumnSpace[N]): ColumnSpace[N] = new ColumnSpace(getVectorSet() - that.getVectorSet()) //todo correct?
     def opposite(): ColumnSpace[N] = new ColumnSpace(getVectorSet().opposite())
     def scale(factor: Double): ColumnSpace[N] = new ColumnSpace(getVectorSet().scale(factor))
     def scale(factor: N): ColumnSpace[N] = new ColumnSpace(getVectorSet().scale(factor))

     override def toString: String = getVectorSet().toString
}



class NullSpace[N <: Number[N]: TypeTag](vset: VectorSet[N])
     extends AbelianGroup[NullSpace[N]] //with Ring[NullSpace[N]] with Monoid[NullSpace[N]]
          with VectorSpace[NullSpace[N], N] with BanachSpace[NullSpace[N], N]
          with LinearIndependence[NullSpace[N], N]
          with BasisSpace[NullSpace[N], N] with Dimension[NullSpace[N], N]
          with Orthonormal[NullSpace[N], N] {

     private val kernelMat: Matrix[N] = makeBasisMatrix()

     def getVectorSet(): VectorSet[N] = vset
     def getMatrix(): Matrix[N] = vset

     def isInNullSpace(b: Vector[N]): Boolean = kernelMat.isSpanned(new VectorSet(b))
     def getVectorInNullSpace(coefs: Vector[N]): Vector[N] = kernelMat.getVectorInSpace(coefs)
     def nullity(): Int = dimension()
     def dimension(): Int = kernelMat.numCols
     def linearlyIndependent(): Boolean = getMatrix().linearlyIndependent() //note: is rowspace cols linindep?
     def isBasisOfSpaceWith(dim: Int): Boolean = kernelMat.spansSpaceWith(dim) && kernelMat.linearlyIndependent()
     def basis(): NullSpace[N] = kernel()
     //todo will this always be an option? Isn't it always guaranteed to at least always have  unique OR infinite
     // solution?
     def kernel(): NullSpace[N] = new NullSpace(kernelMat)
     private def makeBasisMatrix(): Matrix[N] = {
          val zeroVec: Vector[N] = Vector.ZERO[N](getVectorSet().numRows)
          val op = new AugmentedMatrix(getVectorSet(), zeroVec).solve()
          Matrix.fromLists[N](op.get.getColumns().map(vec => vec.toList.map(e => e.asInstanceOf[N])):_*)
     }

     def norm(): N = kernelMat.norm()
     def normalize(): NullSpace[N] = new NullSpace(kernelMat.normalize())
     def isNormalized(): Boolean = kernelMat.isNormalized()
     def orthogonalize(): NullSpace[N] = new NullSpace(kernelMat.orthogonalize())
     def isOrthogonal(): Boolean = kernelMat.isOrthogonal()
     def isOrthogonalTo(that: NullSpace[N]): Boolean = kernelMat.isOrthogonalTo(that.getMatrix())

     def ZERO(): NullSpace[N] = new NullSpace(getVectorSet().ZERO())
     def +(that: NullSpace[N]): NullSpace[N] = new NullSpace(getVectorSet() + that.getVectorSet()) //todo correct?
     def -(that: NullSpace[N]): NullSpace[N] = new NullSpace(getVectorSet() - that.getVectorSet()) //todo correct?
     def opposite(): NullSpace[N] = new NullSpace(getVectorSet().opposite())
     def scale(factor: Double): NullSpace[N] = new NullSpace(getVectorSet().scale(factor))
     def scale(factor: N): NullSpace[N] = new NullSpace(getVectorSet().scale(factor))

     override def toString: String = getVectorSet().toString
}

trait LinearTransformation

//trait Rank[T <: LinearTransformation]

