package org.linalg.studies.linearalgebralibrary.matrix


import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.util.Implicits._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.vector._
import org.linalg.studies.linearalgebralibrary.factorization._

import org.apache.commons.lang3.StringUtils
import scala.collection.IndexedSeq
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._
import scala.util.control.Breaks._




class Matrix[N <: Number[N]: TypeTag](cols: Vector[N]*) extends VectorSet[N](cols:_*) {


     def this(mat: Matrix[N]) = this(mat.getColumns():_*)
     def this(rowNum:Int, colNum:Int) = this(Matrix.ZERO[N](rowNum, colNum))


     //key: doing divide and inverse without using option like above (so relying on getting left or right inverse
     //todo specify later if it is right or left inverse
     def /(that: Matrix[N]): Matrix[N] = this * that.inverse()

     def inverse(): Matrix[N] = new AugmentedMatrix[N](this, Matrix.IDENTITY(this)).solve().get

     def transpose(): Matrix[N] = new Matrix(getRows():_*)

     def adjoint(): Matrix[N] = {
          //definition 6.19: adjoint = transpose(cofactor) if matrix is square.
          val mat: ListBuffer[Vector[N]] = ListBuffer() //ListBuffer.fill[N](numRows, numCols)(Number.ZERO[N])

          for(r <- 0 until numRows){
               val row: Vector[N] = Vector(numCols)
               mat += row
               for(c <- 0 until numCols){
                    val cof = ((r + c) % 2 == 0) match {
                         case true => this.minor(r, c)
                         case false => this.minor(r, c).opposite()
                    }
                    row.set(c, cof)
               }
          }
          Matrix(mat:_*) //implicit transpose (we could have done (matrix (rowtocol(mat)).transpose
     }

     def cofactor(): Matrix[N] ={
          val indexes: IndexedSeq[(Int, Int)] = for(r <- 0 until numRows; c <- 0 until numCols) yield (r, c)
          val cofactors: List[N] = indexes.map(indexPair => cofactor(indexPair._1, indexPair._2)).toList
          Matrix.fromList(numRows, numCols, cofactors).transpose()
     }

     def cofactor(r:Int, c:Int): N = ((r + c) % 2 == 0) match {
          case true => minor(r, c)
          case false => minor(r, c).opposite()
     }

     def minor(): Matrix[N] ={
          val indexes: IndexedSeq[(Int, Int)] = for(r <- 0 until numRows; c <- 0 until numCols) yield (r, c)
          val minors: List[N] = indexes.map(indexPair => minor(indexPair._1, indexPair._2)).toList
          Matrix.fromList(numRows, numCols, minors).transpose() //isrow=true since we travel along rows.
     }

     def minor(r: Int, c: Int): N ={
          //note: new matrix has numRows - 1 columns
          //note: row v has length numCols - 1
          val mat: ListBuffer[Vector[N]] = ListBuffer()
          for(k1 <- 0 until numRows){
               if(k1 != r){
                    val row: Vector[N] = this.getRows()(k1)
                    val v: Vector[N] = Vector(numCols - 1)
                    mat += v

                    var k: Int = 0
                    for(k2 <- 0 until numCols){
                         if(k2 != c) {
                              v.set(k, row.get(k2))
                              k = k + 1
                         }
                    }
               }
          }
          Matrix(mat:_*).transpose().determinant()
     }

     /*val det = complexMatrixJSCI.determinant()
     Number.toNumber(Complex(det.getReal, det.getImaginary).asInstanceOf[N])*/
     def determinant(): N = {
          //proposition 6.12: if square then A * adj(A) = det(A), proof page 449
          //note but don't actually implementat that way since adjoint depends on determinant
          //todo what to do if not square? implement?
          new LUFactorization[N](this).determinant()
     }

     def trace(): N = this.getDiagonal().elementSum

     def conjugateTranspose(): Matrix[N] = {
          Number.runtimeType[N] match {
               case "Real" => this
               case "Rational" => this
               case "Complex" => Matrix.fromList(numCols, numRows, this.transpose().toList
                    .map(e => e.asInstanceOf[Complex].conjugate().asInstanceOf[N]))
          }
     }

     //def toVectorSet: VectorSet[N] = this.asInstanceOf[VectorSet[N]] // todo check if it works

     //key find way to eliminate this (or do it more elegantly)
     //key not using it makes val squaremat = matrix.copy complain since vecset is higher than squaremat
    /* def toSquareMatrix: SquareMatrix[N] = {
          if(numRows != numCols) throw new Exception("Not square -- cannot make it object of class square matrix")

          new SquareMatrix(this)
     }*/
}




object Matrix {
     def ZERO[N <: Number[N] : TypeTag](mat: Matrix[N]): Matrix[N] =
          Matrix.fromBuffers(ListBuffer.fill[N](mat.numCols, mat.numRows)(Number.ZERO[N]):_*)

     def ZERO[N <: Number[N] : TypeTag](nrows:Int, ncols:Int): Matrix[N] =
          Matrix.fromBuffers(ListBuffer.fill[N](ncols, nrows)(Number.ZERO[N]):_*)

     def IDENTITY[N <: Number[N] : TypeTag](size:Int): Matrix[N] ={
          val list = ListBuffer.fill[N](size, size)(Number.ZERO[N])
          for(r <- 0 until size) {for(c <- 0 until size) if(r == c) list(r)(c) = Number.ONE[N] }
          Matrix.fromBuffers(list.toList:_*)
     }

     //makes identity matrix using which is bigger - numrows or numcols
     def IDENTITY[N <: Number[N] : TypeTag](mat: Matrix[N]): Matrix[N] ={
          if(mat.numCols == mat.numCols && mat.numCols == 1) new Matrix[N](new Vector[N](Number.ONE[N]))
          val size = Math.max(mat.numCols, mat.numRows)
          val list = ListBuffer.fill[N](size, size)(Number.ZERO[N])
          for(r <- 0 until size) {for(c <- 0 until size) if(r == c) list(r)(c) = Number.ONE[N] }
          Matrix.fromBuffers(list.toList:_*)
     }
     def apply[N <: Number[N] : TypeTag](nr:Int, nc:Int): Matrix[N] = Matrix.ZERO[N](nr, nc)

     def apply[N <: Number[N] : TypeTag](cols: Vector[N]*): Matrix[N] = new Matrix(cols:_*)

     //def apply[N <: Number[N]: TypeTag](vset: VectorSet[N]): Matrix[N] = Matrix(vset.getColumns():_*)

     def fromBuffers[N <: Number[N] : TypeTag](buffs: ListBuffer[N]*): Matrix[N] =
          new Matrix(buffs.map(list => new Vector(list:_*)):_*)

     def fromLists[N <: Number[N] : TypeTag](lists: List[N]*): Matrix[N] =
          new Matrix(lists.map(list => new Vector(list:_*)):_*)

     //todo why do the matrix[rational] and matrix[complex] turn errorish matrix[nothing] when i name this apply?
     def fromList[N <: Number[N]: TypeTag](nr:Int, nc:Int, list: Seq[N]/*, isRow: Boolean*/): Matrix[N] ={
          //assume data is along column
          Matrix.fromLists(list.grouped(nc).toList.map(_.toList):_*)
     }

}








//------------------------------------------------------------------------------------------------------------------------------


class AugmentedMatrix[N <: Number[N]: TypeTag](A: Matrix[N], B: Matrix[N])
     extends Matrix[N](A.combine(B).getColumns():_*) {

     private val rrefThis: Matrix[N] = this.reducedRowEchelon() //note implicit vecset => matrix
     private val rrefA: Matrix[N] = A.reducedRowEchelon() //note implicit vecset => matrix
     private val rrefB: Matrix[N] = Matrix(rrefThis.getColumns().takeRight(B.numCols):_*)

     //todo make apply methods for these
     //note using explicit method since using implicit would cause constructor error
     def this(A: Matrix[N], b: Vector[N]) = this(A, Matrix(b))
     def this(A: Matrix[N]) = this(A, Matrix.ZERO[N](A.numRows, 1))


     def getA(): Matrix[N] = A
     def getB(): Matrix[N] = B

     def isConsistent(): Boolean = !isInconsistent()
     def isInconsistent(): Boolean = rrefThis.getRows()
          .exists(row =>
               row.toBuffer.init.forall(_.isZero) &&
                    row.toBuffer.drop(A.numCols).exists(e => !e.isZero))

     def hasNoSolution(): Boolean = isInconsistent()
     def hasUniqueSolution(): Boolean = rrefA == Matrix.IDENTITY(rrefA)
     def hasInfiniteSolutions(): Boolean = rrefA != Matrix.IDENTITY(rrefA)

     private def infiniteSolutionSolver(): Matrix[N] ={
          //get the indices of free cols
          val freeIndices: Array[Int] = Util.GenOps.getIndicesOfFreeColumns(rrefA)
          //get the freecolumns and attach to each column another zero vector to make it length = numcolsrref
          val freeCols: List[Vector[N]] = rrefA.getColumns().zipWithIndex
               .filter(colIndexPair => freeIndices.contains(colIndexPair._2)).map(_._1).toList

          //STEP 1: make the matrix of free cols
          var free: VectorSet[N] = new Matrix[N](freeCols:_*)
          //remove any zero rows
          free = Matrix(free.getRows().filterNot(vec => vec.isZero):_*).transpose() //transpose so rows again
          //STEP 2: minus the B rows with nonzero free rows (Brows - freerows), this can just be multiplied by -1
          // since if hwe have just free variable cols then the constants will not minus these.
          free = free.scale(-1)
          //new Matrix(B.getRows().take(free.numRows).zip(free.getRows()).map(p => p._1 - p._2):_*).transpose()

          // make new "matrix" from listbuffer that is old rref transposed with numcol = numfree and zeroes
          // everywhere but in row positions where free col positions we put rows of identity matrix
          val id: ListBuffer[ListBuffer[N]] = Matrix.IDENTITY[N](free.numCols).getRows().map(_.toBuffer)
          val sol: ListBuffer[ListBuffer[N]] = ListBuffer.fill[N](rrefA.numCols, free.numCols)(Number.ZERO[N])
          val freeRows: ListBuffer[ListBuffer[N]] = free.getRows().map(_.toBuffer)
          //fill the row pos with identity rows corresponding to free col pos
          var freeRowIndex: Int = 0
          var idRowIndex: Int = 0
          var r:Int = 0
          while(r < sol.length) {
               if(freeIndices.contains(r)) {
                    breakable {
                         if(idRowIndex >= id.length) break
                         //else
                         sol(r) = id(idRowIndex)
                         idRowIndex = idRowIndex + 1
                    }
               } else {
                    breakable {
                         if(freeRowIndex >= freeRows.length) break
                         //else, it's a zero row and then put the free col rows in it
                         sol(r) = freeRows(freeRowIndex)
                         freeRowIndex = freeRowIndex + 1
                    }
               }
               r = r + 1
          }

          val solution: Matrix[N] = Matrix.fromBuffers(sol:_*).transpose()

          //then add the B cols to the front of our solution, never the case for kernel where B={0}
          if(B.isZero())
               solution
          else {
               //else making constants column and elongating it such that it is as long as solution numrows
               // Step 1: first get the pivot indexes (where pivot 1)
               val indices: Array[Int] = (0 until rrefA.numCols).toArray
               val pivotIndices: Array[Int] = indices.diff(freeIndices)
               // Step 2: zip pivotindices with rrefB - assert always will be same length since
               // it's sliced from rrefThis. Filter to get tuples with nonzero rrefB elements.
               val tuples = pivotIndices.zip(Util.GenOps.expressColsAsRows(rrefB.toListOfLists))
               val tuplesNoZeroRows = tuples.filter({
                    case (index, elems) => !elems.forall(_ == Number.ZERO[N])
               })
               // Step 3: fill zeroes between the elements indices.
               val maxIndex: Int = tuplesNoZeroRows.map(_._1).max // get max index to make list
               var newRrefB: List[List[N]] = Util.GenOps.expressColsAsRows(
                    Matrix.ZERO[N](maxIndex + 1, rrefB.numCols).toListOfLists)
               // inserting the elements in the tuples at the indices.
               for((index, elems) <- tuplesNoZeroRows){
                    newRrefB = Util.GenOps.insert(elems, index, newRrefB)
               }
               newRrefB = Util.GenOps.expressRowsAsCols(newRrefB)
               Matrix.fromLists(newRrefB:_*).combine(solution) //note implicit vecset => matrix
          }
     }


     def solve(): Option[Matrix[N]] ={
          if(hasNoSolution()) None
          else if(hasUniqueSolution()) Some(Matrix[N](rrefThis.getColumns().takeRight(B.numCols):_*))
          else Some(infiniteSolutionSolver())
     }


     override def toString: String ={
          val colsStr: ListBuffer[List[String]] = this.getColumns().map(vec => vec.toList.map(elem => elem
               .toString))

          // max widths measured per col
          val maxWidths: ListBuffer[Int] = colsStr.map(vec => vec.reduceLeft((acc,y) =>
               if(acc.length > y.length) acc else y)).map(_.length)

          val maxWidthsTwoDim: ListBuffer[List[Int]] = maxWidths.map(elem => List.fill(numRows)(elem))

          // col center length tupled with actual matrix col element in vector of vectors
          val pairs = colsStr.zip(maxWidthsTwoDim).map(pair => pair._1.zip(pair._2))
          val alignedCols/*: ListBuffer[List[String]]*/ = pairs.map(vec =>
               vec.map(pair => StringUtils.leftPad(pair._1.toString, pair._2)))

          var sepAlignedCols = alignedCols.take(A.numCols) += List.fill[String](A.numRows)("|")
          sepAlignedCols = sepAlignedCols ++ alignedCols.drop(A.numCols)

          // note: let maxWidth + 2 separate the numbers in the row
          if(numRows == 1)
               return "\n{" + sepAlignedCols.transpose.head.mkString("  ") + "}"
          val firstRow: String = "\n/ " + sepAlignedCols.transpose.head.mkString("  ") + " \\\n"
          val lastRow: String = "\\ " + sepAlignedCols.transpose.last.mkString("  ") + " /"
          val middleRows: ListBuffer[String] = sepAlignedCols.transpose.tail.init
               .map(list => "| " + list.mkString("  ") + " |\n")

          firstRow + middleRows.mkString + lastRow
     }
}


object AugmentedMatrix {
     def apply[N <: Number[N]: TypeTag](A: Matrix[N], B: Matrix[N]): AugmentedMatrix[N] ={
          new AugmentedMatrix[N](A, B)
     }

     def apply[N <: Number[N]: TypeTag](A: Matrix[N], b: Vector[N]): AugmentedMatrix[N] ={
          new AugmentedMatrix[N](A, b)
     }
}




//------------------------------------------------------------------------------------------------------------------------------


/**
  * TODO = figure out when to override (special cases eigenvalues diagonal matrix, e.g.) to show through implementation
  * todo = that we can solve it simpler with these special cases of matrices
  */

class SquareMatrix[N <: Number[N]: TypeTag](mat: Matrix[N]) extends Matrix[N](mat.getColumns():_*) {

     val size: Int = mat.numRows

     //todo make more constructors
     def this(cols: Vector[N]*) = this(Matrix(cols:_*))
     def this(vset: VectorSet[N]) = this(vset.getColumns():_*)
     def this(rowNum:Int, colNum:Int) = this(Matrix.ZERO[N](rowNum, colNum))

     //todo make more squarematrx ops functions (det and pow): http://bcuccioli.github.io/MatrixLib/doc/
//     def determinant(): F
//     def raiseToPower(p: Int): SquareMatrix[F]
     override def inverse(): SquareMatrix[N] = new SquareMatrix(mat.inverse())

}

object SquareMatrix {

     def ZERO[N <: Number[N] : TypeTag](smat: SquareMatrix[N]): SquareMatrix[N] =
          new SquareMatrix(Matrix.ZERO[N](smat.numRows, smat.numCols))

     def ZERO[N <: Number[N] : TypeTag](nrows:Int, ncols:Int): SquareMatrix[N] =
          new SquareMatrix(Matrix.ZERO[N](nrows, ncols))

     def IDENTITY[N <: Number[N] : TypeTag](size:Int): SquareMatrix[N] =
          new SquareMatrix(Matrix.IDENTITY[N](size))

     //makes identity matrix using which is bigger - numrows or numcols
     def IDENTITY[N <: Number[N] : TypeTag](smat: SquareMatrix[N]): SquareMatrix[N] =
          new SquareMatrix(Matrix.IDENTITY[N](Matrix(smat.getColumns():_*)))

     def DIAGONAL[N <: Number[N]: TypeTag](diag: Vector[N]): SquareMatrix[N] ={
          val mat: ListBuffer[ListBuffer[N]] = ListBuffer()
          for(r <- 0 until diag.dimension()){
               for(c <- 0 until diag.dimension()){
                    if(r == c) mat(r)(c) = diag.get(r)
                    else mat(r)(c) = Number.ZERO.asInstanceOf[N]
               }
          }
          new SquareMatrix(Matrix.fromBuffers(mat:_*))
     }

     def DIAGONAL[N <: Number[N]: TypeTag](elem: N, size: Int): SquareMatrix[N] ={
          SquareMatrix.DIAGONAL[N](new Vector(List.fill[N](size)(elem):_*))
     }

     def apply[N <: Number[N] : TypeTag](nr:Int, nc:Int): SquareMatrix[N] =
          new SquareMatrix(Matrix.ZERO[N](nr, nc))

     def apply[N <: Number[N] : TypeTag](cols: Vector[N]*): SquareMatrix[N] = new SquareMatrix(cols:_*)

}


//---------------------------------------------------------------------------------------------------------------

class HessenbergMatrix[N <: Number[N]: TypeTag](smat: SquareMatrix[N])
     extends SquareMatrix[N](Util.MatrixOps.Trans.HessenbergTransformer.makeHessenberg(smat))

//---------------------------------------------------------------------------------------------------------------

class LowerTriangularMatrix[N <: Number[N]: TypeTag](mat: Matrix[N]) extends Matrix[N](mat.getColumns():_*) {
     require(Util.MatrixOps.Id.isLowerTriangular(mat))
}

//---------------------------------------------------------------------------------------------------------------

class UpperTriangularMatrix[N <: Number[N]: TypeTag](mat: Matrix[N]) extends Matrix[N](mat.getColumns():_*) {
     require(Util.MatrixOps.Id.isUpperTriangular(mat))

     def this(mat: VectorSet[N]) = this(new Matrix(mat))
}

//---------------------------------------------------------------------------------------------------------------

class UnitaryMatrix[N <: Number[N]: TypeTag](smat: SquareMatrix[N]) extends SquareMatrix[N](smat) {
     require(Util.MatrixOps.Id.isUnitary(smat))
}

//---------------------------------------------------------------------------------------------------------------

class OrthogonalMatrix[N <: Number[N]: TypeTag](smat: SquareMatrix[N]) extends SquareMatrix[N](smat) {
     require(Util.MatrixOps.Id.isOrthogonal(smat))
}

//---------------------------------------------------------------------------------------------------------------

class HermitianMatrix[N <: Number[N]: TypeTag](smat: SquareMatrix[N]) extends SquareMatrix[N](smat) {
     require(Util.MatrixOps.Id.isHermitian(smat))
}

//---------------------------------------------------------------------------------------------------------------

class SymmetricMatrix[N <: Number[N]: TypeTag](smat: SquareMatrix[N]) extends SquareMatrix[N](smat) {
     require(Util.MatrixOps.Id.isSymmetric(smat))
}

/*
XXXXXXXXXX class SimilarMatrix[N <: Number[N] : TypeTag](cols: Vector[N]*) extends Matrix[N](cols:_*)
XXXXXXXXXX class HessianMatrix[N <: Number[N] : TypeTag](cols: Vector[N]*) extends Matrix[N](cols:_*)
XXXXXXXXXXX class UpperHessenbergMatrix[N <: Number[N] : TypeTag](cols: Vector[N]*) extends Matrix[N](cols:_*)

class DiagonalMatrix[N <: Number[N] : TypeTag](diagonal: Vector[N]) extends Matrix[N](diagonal)

class HilbertMatrix[N <: Number[N] : TypeTag](cols: Vector[N]*) extends Matrix[N](cols:_*)

class JacobianMatrix[N <: Number[N] : TypeTag](cols: Vector[N]*) extends Matrix[N](cols:_*)

*/


