package org.linalg.studies.linearalgebralibrary.matrix


import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.util.Implicits._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
import org.linalg.studies.linearalgebralibrary.vector._
import org.linalg.studies.linearalgebralibrary.theory._



import matrixLib.exception.DimensionMismatchException
import org.apache.commons.lang3.StringUtils
import scala.reflect.runtime.universe._
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._



//key make adjugate funciton


class VectorSet[N <: Number[N]: TypeTag](cols: Vector[N]*)
     extends VectorSpace[VectorSet[N], N] with BanachSpace[VectorSet[N], N] with AbelianGroup[VectorSet[N]] //with
          // Monoid[VectorSet[N]] with Ring[VectorSet[N]]
          with Dimension[VectorSet[N], N] with Rank[VectorSet[N], N]
          with LinearIndependence[VectorSet[N], N]
          with Span[VectorSet[N], N] with Basis[VectorSet[N], N]
          with Orthonormal[VectorSet[N], N] {

     val numRows: Int = cols.head.dimension()
     val numCols: Int = cols.length
     private val matrix: ListBuffer[Vector[N]] = ListBuffer(cols:_*)


     def this(rowNum:Int, colNum:Int) = this(VectorSet[N](rowNum, colNum).getColumns():_*)
     def this(vset: VectorSet[N]) = this(vset.getColumns():_*)
     //def this(buffs: ListBuffer[N]*) = this(VectorSet.fromBuffers[N](buffs:_*).getColumns():_*)
     //def this(lists: List[N]*) = this(VectorSet.fromLists[N](lists:_*).getColumns():_*)
     def this(rowNum:Int, colNum:Int, oneList: List[N])
          = this(VectorSet.fromList[N](rowNum, colNum, oneList).getColumns():_*)



     //basis: det = 0 is same as saying cols are lin indep (pg 126 theorem 2.3.8 in howard)
     def basis(): Option[VectorSet[N]] = isBasisOfSpaceWith(this.numRows) match {
               case false => None
               case true => Some(reducedRowEchelon()) //todo help do you need to row reduce or just return `this`??
          }
     def isBasisOfSpaceWith(dim: Int): Boolean = spansSpaceWith(dim) && linearlyIndependent()
     def rank(): Int = new RowSpace(this).dimension()
     def isFullRank(): Boolean = rank() == numRows
     //todo check if correct implementation???? check pg 222 howard
     def dimension(): Int = if(!basis().isDefined) 0 else basis().get.numCols //num basis vectors for a vecspace
     def getRowSpaceBasis(): RowSpace[N] = new RowSpace(this).basis()
     def getColSpaceBasis(): ColumnSpace[N] = new ColumnSpace(this).basis()
     def spansSpaceWith(dim: Int): Boolean = dim == this.numCols
     def isSpanned(vset: VectorSet[N]): Boolean = new AugmentedMatrix[N](this, vset).solve().isDefined
     def getVectorInSpace(coefs: Vector[N]): Vector[N]
          = getColumns().zip(coefs.toList).map(p => p._1.scale(p._2)).reduceLeft(_ + _)
     def getSpanningCoefficients(vset: VectorSet[N]): VectorSet[N] ={ //if none, gives zero vset in vset's size
          if(isSpanned(vset)){
               return new AugmentedMatrix[N](this, vset).solve().get
          }
          VectorSet.ZERO[N](vset)
     }

     def linearlyIndependent(): Boolean ={
          val rref: VectorSet[N] = reducedRowEchelon()
          rref == VectorSet.IDENTITY(rref)
     }

     def norm(): N = Number.ZERO[N] // todo calculate the norm and patch up Norm class in matrixLib
     /**
       * Calculate the 2-norm of a matrix, which is the largest singular value.
*X = [2 0 1;-1 1 0;-3 3 0];
*n = norm(X)
*n = 4.7234
       */
     def normalize(): VectorSet[N] = new VectorSet[N](getColumns().map(vec => vec.normalize()):_*)
     def isNormalized(): Boolean = normalize() == this
     def isMultipleOf(that: VectorSet[N]): Boolean = getColumns().zip(that.getColumns()).forall(p => p._1.isMultipleOf(p._2))
     def isOrthogonalTo(that: VectorSet[N]): Boolean = toVector.isOrthogonalTo(that.toVector) //todo right? or combos?
     def orthogonalize(): VectorSet[N] = VectorSet.ZERO[N](this) //todo help

     // todo )
     //     //using gram-schmidt
     //     def orthonormalColSpaceBasis(): Matrix[N] = convertIn(complexMatrixLib.orthonormalize())
     //     def orthonormalRowSpaceBasis(): Matrix[N] // todo



     // vector space axioms
     def ZERO(): VectorSet[N] = VectorSet.ZERO[N](this)

     def add(factor: N): VectorSet[N] = VectorSet.fromLists(matrix.map(col => col.toList.map(e => e + factor)):_*)
     def subtract(factor: N): VectorSet[N] = VectorSet.fromLists(matrix.map(col => col.toList.map(e => e - factor)):_*)
     def scale(factor: N): VectorSet[N] = new VectorSet(this.matrix.map(vec => vec.scale(factor)):_*)
     def scale(factor: Double): VectorSet[N] = new VectorSet(this.matrix.map(vec => vec.scale(factor)):_*)
     def divide(factor: N): VectorSet[N] = VectorSet.fromLists(matrix.map(col => col.toList.map(e => e / factor)):_*)

     def +(that: VectorSet[N]): VectorSet[N] = {
          if(that.numRows != numRows || that.numCols != numCols) throw new DimensionMismatchException()
          //add elementwise
          new VectorSet(this.matrix.zip(that.matrix).map(vecPair => vecPair._1 + vecPair._2):_*)
     }

     def -(that: VectorSet[N]): VectorSet[N] = {
          if(that.numRows != numRows || that.numCols != numCols) throw new DimensionMismatchException()
          //add elementwise
          new VectorSet(this.matrix.zip(that.matrix).map(vecPair => vecPair._1 - vecPair._2):_*)
     }

     def *(that: VectorSet[N]): VectorSet[N] = {
          if(numCols != that.numRows) throw new DimensionMismatchException("incompatible dimensions for multiplying")

          val prod: ListBuffer[ListBuffer[N]] = ListBuffer.fill[N](that.numCols, numRows)(Number.ZERO[N])
          for(r <- 0 until numRows){
               for(c <- 0 until that.numCols){
                    for(k <- 0 until numCols){
                         //note it would be get(r, k) and get(k, c) if our internal matrices were represented with
                         // rows, but they are represented in cols so use this inverted call.
                         prod(c)(r) = (prod(c)(r) + this.get(k, r)) * that.get(c, k)
                    }
               }
          }
          VectorSet.fromBuffers(prod:_*)
     }

     // TODO use linear algebra formula.
     //def ^(exp: Int): Matrix[N] =
     def opposite(): VectorSet[N] = VectorSet.fromLists(this.toList.map(n => n.opposite()))


     def rowEchelon(): VectorSet[N] = rref(reduced = false)

     def reducedRowEchelon(): VectorSet[N] = {
          val theRRef = rref(reduced=true)
          new VectorSet(Util.GenOps.expressRowsAsCols(theRRef.getNonZeroRows()):_*)
     }

     def isZero(): Boolean = getRows().forall(_.isZero())

     //note  another way: see if elements at indexes starting at zero and ever size+1 are 1 and everywhere else zero
     // note: so if size=4 then at every index 0,5,10,15.. must be 1 everywhere else 0.
     def isIdentity(): Boolean = {
          if(!isSquare()) return false

          for(r <- 0 until numRows){
               for(c <- 0 until numCols){
                    if((r != c && !get(r,c).isZero) || (r == c && get(r,c) != 0)){
                         return false //found a violation
                    }
               }
          }
          true // no violation
     }

     def isSquare(): Boolean = numRows == numCols

     //tells whether it is A^T A = I or not
     //NOTE implicit from vecset to matrix being used here
     def isOrthogonal(): Boolean = {
          if(!isSquare()) return false
          (this * this.transpose()).isIdentity()
     }

     //Tells whether the matrix is unitary (its inverse is its conjugate transpose)
     //NOTE implicit from vecset to matrix being used here
     def isUnitary(): Boolean = {
          if(!isSquare()) return false
          (this * this.conjugateTranspose()).isIdentity()
     }

     // does not have an inverse (singular). But has inverse if:
     // * cols are linearly independent
     // * matrix is square && full rank.
     // * matrix is not square and determinant == 0 todo fix if not square then we can't find determinant...
     /////!this.isSquare && Matrix(this).determinant() == 0
     def isSingular(): Boolean = !linearlyIndependent()

     def isUnderDetermined(): Boolean = numRows < numCols

     def isOverDetermined(): Boolean = numRows > numCols


     def get(row: Int, col: Int): N = matrix(col).get(row)
     def set(row: Int, col: Int)(value: N): Unit = matrix(col).set(row, value)

     def getCol(c:Int): Vector[N] = matrix(c)
     def getColumns(): ListBuffer[Vector[N]] = matrix
     def getColsFrom(c:Int): ListBuffer[Vector[N]] = getColumns().drop(c)
     def getColsBetween(c1:Int, c2:Int): ListBuffer[Vector[N]] = getColumns().drop(c1).take(c2-c1+1)

     //inthis case since we have cols in `matrix` the function will express cols as Rows
     def getRow(r:Int): Vector[N] = Util.GenOps.expressRowsAsCols(matrix).apply(r)
     def setRow(r: Int, row: Vector[N]): Unit = {
          for(c <- 0 until numCols){
               this.set(r, c)(row.get(c))
          }
     }
     def getNonZeroRows(): ListBuffer[Vector[N]] = getRows().filterNot(row => row.isZero)
     def getRowsFrom(r:Int): ListBuffer[Vector[N]] = getRows().drop(r)
     def getRowsBetween(r1:Int, r2:Int): ListBuffer[Vector[N]] = getRows().drop(r1).take(r2-r1+1)
     def getRows(): ListBuffer[Vector[N]] = {
          val rows: ListBuffer[Vector[N]] = ListBuffer()
          for(r <- 0 until numRows) rows += new Vector(matrix.map(colVec => colVec.get(r)):_*)
          rows
     }

     //adds rows until a certain number of rows `length` is reached.
     def addZeroRowsUntil(length: Int): VectorSet[N] ={
          val vecSet = getColumns() ++ ListBuffer.fill[Vector[N]](length - numRows)(Vector.ZERO[N](numCols))
          new VectorSet(vecSet.reduceLeft((acc, y) => acc attach y))
     }

     //combines them vertically
     def combine(that: VectorSet[N]): VectorSet[N] = new VectorSet((this.getColumns() ++ that.getColumns()):_*)

     def getDiagonal(): Vector[N] = {

          val dim: Int = Math.min(numRows, numCols).toInt
          val vec: Vector[N] = Vector(dim)
          for(i <- 0 until dim) vec.set(i, this.get(i, i))
          vec
     }

     def copy(): VectorSet[N] = new VectorSet(matrix:_*)


     def get() = matrix

     def toList: List[N] = matrix.reduceLeft((acc,y) => acc attach y).toList

     def toListOfLists: List[List[N]] = matrix.map(vec => vec.toList).toList

     def toBuffer: ListBuffer[N] = matrix.reduceLeft((acc,y) => acc attach y).toBuffer

     def toVector: Vector[N] = new Vector[N](toList:_*)

     //def toMatrix: Matrix[N] = new Matrix(getColumns():_*)

     def ==(that: VectorSet[N]): Boolean = this.toList.zip(that.toList).forall(pair => pair._1 == pair._2)

     override def toString: String ={
          val colsStr: ListBuffer[List[String]] = this.getColumns().map(vec => vec.toList.map(elem => elem.toString))

          // max widths measured per col
          val maxWidths: ListBuffer[Int] = colsStr.map(vec => vec.reduceLeft((acc,y) =>
               if(acc.length > y.length) acc else y)).map(_.length)

          val maxWidthsTwoDim: ListBuffer[List[Int]] = maxWidths.map(elem => List.fill(numRows)(elem))

          // col center length tupled with actual matrix col element in vector of vectors
          val pairs = colsStr.zip(maxWidthsTwoDim).map(pair => pair._1.zip(pair._2))
          val aligned = pairs.map(vec => vec.map(pair => StringUtils.leftPad(pair._1.toString, pair._2)))

          // note: let maxWidth + 2 separate the numbers in the row
          if(numRows == 1)
               return "\n{" + aligned.transpose.head.mkString("  ") + "}"
          val firstRow: String = "\n/ " + aligned.transpose.head.mkString("  ") + " \\\n"
          val lastRow: String = "\\ " + aligned.transpose.last.mkString("  ") + " /"
          val middleRows: ListBuffer[String] = aligned.transpose.tail.init
               .map(list => "| " + list.mkString("  ") + " |\n")

          firstRow + middleRows.mkString + lastRow
     }


     // -------------------------------------------------------
     //utilities

     private def rref(reduced: Boolean): VectorSet[N] ={
          var echelonMatrix: VectorSet[N] = this.copy()
          var lead: Int = 0

          breakable {
               for(r <- 0 until numRows){
                    if(lead >= numCols){
                         break
                    }
                    var i: Int = r
                    while (echelonMatrix.get(i, lead).isZero) { //then find the pivot element
                         i = i + 1
                         if (i == numRows){
                              i = r
                              lead = lead + 1
                              if(lead == numCols) { //then we have found last pivot
                                   return echelonMatrix
                              }
                         }
                    }

                    //swap rows i and r
                    if(i != r) echelonMatrix = Util.GenOps.swapRows(i, r, echelonMatrix)

                    //divide row r by rref[r][lead]
                    echelonMatrix = Util.GenOps.scaleRow(r, echelonMatrix.get(r, lead).inverse(), echelonMatrix)

                    for(j <- 0 until numRows){ //back-substitute upwards
                         if(j != r){  //subtract row r * -rref[j][lead] from row j
                              echelonMatrix = Util.GenOps.sumRows(j, r, echelonMatrix.get(j, lead).opposite(), echelonMatrix)
                         }
                    }

                    lead = lead + 1 //now looking for a pivot further to the right
               }
          }

          echelonMatrix
     }
}



object VectorSet {
     def ZERO[N <: Number[N] : TypeTag](mat: VectorSet[N]): VectorSet[N] =
          VectorSet.fromBuffers(ListBuffer.fill[N](mat.numCols, mat.numRows)(Number.ZERO[N]):_*)

     def ZERO[N <: Number[N] : TypeTag](nrows:Int, ncols:Int): VectorSet[N] =
          VectorSet.fromBuffers(ListBuffer.fill[N](ncols, nrows)(Number.ZERO[N]):_*)

     def IDENTITY[N <: Number[N] : TypeTag](size:Int): VectorSet[N] ={
          val list = ListBuffer.fill[N](size, size)(Number.ZERO[N])
          for(r <- 0 until size) {for(c <- 0 until size) if(r == c) list(r)(c) = Number.ONE[N] }
          VectorSet.fromBuffers(list.toList:_*)
     }

     //makes identity matrix using which is bigger - numrows or numcols
     def IDENTITY[N <: Number[N] : TypeTag](mat: VectorSet[N]): VectorSet[N] ={
          if(mat.numCols == mat.numCols && mat.numCols == 1) new VectorSet[N](new Vector[N](Number.ONE[N]))
          val size = Math.max(mat.numCols, mat.numRows)
          val list = ListBuffer.fill[N](size, size)(Number.ZERO[N])
          for(r <- 0 until size) {for(c <- 0 until size) if(r == c) list(r)(c) = Number.ONE[N] }
          VectorSet.fromBuffers(list.toList:_*)
     }

     //copy constructor
     def apply[N <: Number[N]: TypeTag](vset: VectorSet[N]): VectorSet[N] = vset

     def apply[N <: Number[N] : TypeTag](nr:Int, nc:Int): VectorSet[N] =
          new VectorSet(new Vector(ListBuffer.fill[N](nr * nc)(Number.ZERO[N]):_*))

     def apply[N <: Number[N] : TypeTag](cols: Vector[N]*): VectorSet[N] = new VectorSet(cols:_*)
     //note: biulds a vectorset out of elements.
     /*def apply[N <: Number[N]: TypeTag](elems: N*): VectorSet[N] = {
          Vector.ZERO[N](elems.length)
     }*/

     def fromBuffers[N <: Number[N] : TypeTag](buffs: ListBuffer[N]*): VectorSet[N] =
          new VectorSet(buffs.map(list => new Vector(list:_*)):_*)

     def fromLists[N <: Number[N] : TypeTag](lists: List[N]*): VectorSet[N] =
          new VectorSet(lists.map(list => new Vector(list:_*)):_*)

     //todo why do the matrix[rational] and matrix[complex] turn errorish matrix[nothing] when i name this apply?
     def fromList[N <: Number[N]: TypeTag](nr:Int, nc:Int, list: Seq[N]): VectorSet[N] ={
          //assume data is along column
          VectorSet.fromLists(list.grouped(nc).toList.map(_.toList):_*)
     }
}








/**
  Copy of sedenion's rref which doesn't work for a matrix (see todo note)

  var echelonMatrix: VectorSet[N] = this.copy() // Clone Matrix so algorithm may be performed in place.
          var r: Int = 0
          var c: Int = 0
          while(r < numRows && c < numCols){
               //look for target value in column
               var iMax: Int = r
               for(k <- (r + 1) until numRows) {
                    if (echelonMatrix.get(k, c) > echelonMatrix.get(iMax, c))
                         iMax = k
               }
               if(echelonMatrix.get(iMax, c) != 0){
                    //swap pivot row into place
                    echelonMatrix = echelonMatrix.swapRows(r, iMax) //m[i,j] now contains m[imax, j]
                    //scale pivot row
                    echelonMatrix = echelonMatrix.scaleRow(r, echelonMatrix.get(r, c).inverse()) // m[i,j] now = 1
                    // fill all lower rows with 0
                    for(u <- (r+1) until numRows){
                         echelonMatrix = echelonMatrix.sumRows(u, r, echelonMatrix.get(u,c).opposite()) // m[u,j] now = 0
                    }
                    if(reduced) { //if above cols need filling
                         val reversedIndexes: List[Int] = if(r == 0) List() else (0 to (r-1)).reverse.toList
                         for(u <- reversedIndexes){
                              echelonMatrix = echelonMatrix.sumRows(u, r, echelonMatrix.get(u,c).opposite()) // m[v,j]
                              // now = 0
                         }
                    }
                    r = r+1 // next row


                    //TODO check: my insertion here. Noticed that for pg 103 1.f) matrix rref algo here keeps
                    // todo searching along the zero row and doesn't swich the 0 0 -3 0 1 row with the zero row in time
                    // todo so here I'm carrying out any zero row switches automatically
  NOTE: the insertion threw error on another matrix (index out of bounds)
                    if(echelonMatrix.getNonZeroRows().length < echelonMatrix.numRows){
                         echelonMatrix = Matrix(Util.expressRowsAsCols(echelonMatrix.getNonZeroRows()):_*)
                         //getting rid of the zero row
                    }
               }
               c = c + 1 // next col
          }
          echelonMatrix //Return the transformed matrix.

  */



