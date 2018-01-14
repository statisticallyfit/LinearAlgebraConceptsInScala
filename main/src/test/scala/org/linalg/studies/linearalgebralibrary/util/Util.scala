package org.linalg.studies.linearalgebralibrary.util


import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.util.Implicits._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
import org.linalg.studies.linearalgebralibrary.vector._
import org.linalg.studies.linearalgebralibrary.theory._


import scala.reflect.runtime.universe._
import scala.collection.mutable.ListBuffer
import matrixLib.ComplexNumber
import org.jscience.mathematics.number
import org.jscience.mathematics.vector



/**
  *
  */
//temp util for testing imagebasis in bcuccioli colspace
object Util {

     object GenOps {

          /**
            * Inserts element at position i, leaving list the same length as before.
            */
          def insert[N <: Number[N]](elem: N, i: Int, list: List[N]): List[N] ={
               val (first, second) = list.splitAt(i)
               (first :+ elem) ++: second.tail
          }

          /**
            * Inserts list at position i, leaving list of lists the same length as before.
            */
          def insert[N <: Number[N]](elems: List[N], i: Int, list: List[List[N]]): List[List[N]] ={
               val (first, second) = list.splitAt(i)
               (first :+ elems) ++: second.tail
          }

          def roundTo(double: Double, places:Int): Double ={
               //step 1: multiply by the correct number of zeroes = numplaces
               val factor:Double = ("1" + List.fill[Int](places)(0).mkString).toDouble
               (double * factor).round / factor
          }


          /**
            * Scales a row of a matrix.
            *
            * This method multiplies every value in a given row by a scale factor (scale).
            *
            * @param row The row to be scaled
            * @param scale The scale factor (amount to be multiplied by)
            * @return The new Matrix with the row scaled
            */
          def scaleRow[N <: Number[N]: TypeTag](row: Int, scale: N, vset: VectorSet[N]): VectorSet[N] ={
               val newMatList: ListBuffer[N] = vset.getRows().reduceLeft((accRow,yRow) => accRow attach yRow).toBuffer

               for(i <- row*vset.numCols until ((row+1)*vset.numCols)) {
                    newMatList(i) *= scale
               }

               //converting from row to col representation
               val rows: ListBuffer[Vector[N]] = ListBuffer(newMatList.grouped(vset.numCols).toList
                    .map(list => new Vector(list:_*)):_*)

               new VectorSet(Util.GenOps.expressRowsAsCols[N](rows):_*)
          }

          /**
            * @param rowA The row to be added to.
            * @param rowB The row to add.
            * @param scale The scale factor.
            * @return A new matrix, with the columns changed appropriately.
            */
          def sumRows[N <: Number[N]: TypeTag](rowA: Int, rowB: Int, scale:N, vset: VectorSet[N]): VectorSet[N] ={
               val oldMatList: ListBuffer[N] = vset.getRows().reduceLeft((accRow,yRow) => accRow attach yRow).toBuffer
               val newMatList: ListBuffer[N] = oldMatList
               for(i <- 0 until vset.numCols){ // for each value in rowA
                    newMatList(rowA*vset.numCols + i) += oldMatList(rowB*vset.numCols+i) * scale //
               }

               // using grouped so we get cols again
               val rows: ListBuffer[Vector[N]] = ListBuffer(newMatList.grouped(vset.numCols).toList
                    .map(list => new Vector(list:_*)):_*)

               new VectorSet(Util.GenOps.expressRowsAsCols[N](rows):_*)
          }

          /**
            * @param colA The row to be added to.
            * @param colB The row to add.
            * @param scale The scale factor.
            * @return A new matrix, with the columns changed appropriately.
            */
          def sumCols[N <: Number[N]: TypeTag](colA: Int, colB: Int, scale: N, vset: VectorSet[N]): VectorSet[N] ={
               val oldMatList: ListBuffer[N] = vset.getColumns().reduceLeft((accCol,yCol) => accCol attach yCol).toBuffer
               val newMatList: ListBuffer[N] = oldMatList
               for(i <- 0 until vset.numRows){ // for each value in rowA
                    newMatList(colA*vset.numRows + i) += oldMatList(colB*vset.numRows+i) * scale //
               }

               VectorSet.fromList[N](vset.numRows, vset.numCols, newMatList)
          }

          /**
            * Swaps two rows of a matrix.
            *
            * @param rowA First Row to be swapped
            * @param rowB Second Row to be swapped
            * @return A matrix with rows A and B swapped.
            */
          def swapRows[N <: Number[N]: TypeTag](rowA: Int, rowB: Int, vset: VectorSet[N]): VectorSet[N] ={
               val rows: ListBuffer[Vector[N]] = vset.getRows()
               //swapping
               val temp: Vector[N] = rows(rowA)
               rows(rowA) = rows(rowB)
               rows(rowB) = temp

               new VectorSet(Util.GenOps.expressRowsAsCols[N](rows):_*)
          }

          def swapCols[N <: Number[N]: TypeTag](colA:Int, colB:Int, vset:VectorSet[N]): VectorSet[N] ={
               val cols: ListBuffer[Vector[N]] = vset.getColumns()
               //swapping
               val temp: Vector[N] = cols(colA)
               cols(colA) = cols(colB)
               cols(colB) = temp

               new VectorSet(cols:_*)
          }

          def seqToVecSet[N <: Number[N]: TypeTag](seq: N*): VectorSet[N] = {
               val vset = VectorSet[N](seq.length, 0)
               for(i <- 0 until seq.length) vset.set(i, 0)(seq(i))
               vset
          }

          def seqToVec[N <: Number[N]: TypeTag](seq: Seq[N]): Vector[N] = new Vector(seq:_*)


          //precondition: expects the rref to come from undetermined system -- used for Solver
          def getIndicesOfFreeColumns[N <: Number[N]: TypeTag](rref: VectorSet[N]): Array[Int] ={
               def countNonZero(v: Vector[N]): Int = v.toList.count(e => e != 0)
               //assumes we only have a single element in the vector
               def itsSingleElemIsNotOne(v: Vector[N]): Boolean = countNonZero(v) == 1 &&
                    v.toList.exists(e => e != 1 && e != 0)

               val vecIndexPair: ListBuffer[(Vector[N], Int)] = rref.getColumns().zipWithIndex

               val indices = vecIndexPair.map( {
                    case (v, i) =>
                    if(countNonZero(v) > 1 || itsSingleElemIsNotOne(v))
                         Some(i)
                    else
                         None
               } )
               //need options to preserve type else Array[Any]
               val indices2 = indices.filter(op => op.isDefined).map(_.get).toArray
               indices2
          }

          def expressRowsAsCols[N <: Number[N]: TypeTag](rows: ListBuffer[Vector[N]]): ListBuffer[Vector[N]] ={
               //converting from row to col representation
               val ncol: Int = rows.head.dimension()
               val nrow: Int = rows.length
               val colBuff: ListBuffer[ListBuffer[N]] = ListBuffer.fill[N](ncol, nrow)(Number.ZERO[N])

               for(c <- 0 until ncol){
                    colBuff(c) = rows.map(row => row.get(c))
               }
               colBuff.map(buff => new Vector(buff:_*))
          }

          def expressRowsAsCols[N <: Number[N]: TypeTag](rows: List[List[N]]): List[List[N]] ={
               val result = expressRowsAsCols(ListBuffer(rows.map(list => seqToVec(list)):_*))
               result.map(vec => vec.toList).toList
          }

          def expressColsAsRows[N <: Number[N]: TypeTag](cols: ListBuffer[Vector[N]]): ListBuffer[Vector[N]] ={
               expressRowsAsCols(cols)
          }

          def expressColsAsRows[N <: Number[N]: TypeTag](cols: List[List[N]]): List[List[N]] ={
               val result = expressColsAsRows(ListBuffer(cols.map(list => seqToVec(list)):_*))
               result.map(vec => vec.toList).toList
          }

          type JComplex = org.jscience.mathematics.number.Complex
          type LibMatrix = matrixLib.Matrix
          type JMatrix = org.jscience.mathematics.vector.ComplexMatrix

          //convert from jsci to MY matrix
          //TODO check: making this return runtime number so no longer necessarily complex
          def convertIn[N <: Number[N]: TypeTag](matj: JMatrix): VectorSet[N] ={
               val rowNum = matj.getNumberOfRows
               val colNum = matj.getNumberOfColumns

               val buff: ListBuffer[ListBuffer[N]] = ListBuffer.fill[N](colNum, rowNum)(Number.ZERO[N])

               for(c <- 0 until colNum){
                    val colVec = matj.getColumn(c)
                    val colBuff: ListBuffer[N] = ListBuffer.fill[N](rowNum)(Number.ZERO[N])
                    // turning colVec to listbuffer
                    for(r <- 0 until colVec.getDimension){
                         val elem = colVec.get(r)
                         colBuff(r) = Number.toNumber(elem.getReal, elem.getImaginary)
                    }
                    buff(c) = colBuff
               }
               VectorSet.fromBuffers(buff:_*)
          }

          //convert from matrxlib matrix to MY matrix
          //TODO check: making this return runtime number so no longer necessarily complex
          def convertIn[N <: Number[N]: TypeTag](matlib: LibMatrix): VectorSet[N] ={
               val rowNum = matlib.rows()
               val colNum = matlib.cols()

               val buff: ListBuffer[ListBuffer[N]] = ListBuffer.fill[N](colNum, rowNum)(Number.ZERO[N])

               for(c <- 0 until colNum){
                    val colVec = matlib.getVector(c)
                    val colBuff: ListBuffer[N] = ListBuffer.fill[N](rowNum)(Number.ZERO[N])
                    // turning colVec to listbuffer
                    for(r <- 0 until colVec.dim()){
                         val elem = colVec.getAt(r)
                         colBuff(r) = Number.toNumber[N](elem.Re(), elem.Im()) //todo error
                         // todo -- here???? useless cast
                    }
                    buff(c) = colBuff
               }
               VectorSet.fromBuffers(buff:_*)
          }

          // convert from MY matrix to Jscience
          def convertOutJ[N <: Number[N]: TypeTag](mat: VectorSet[N]): JMatrix ={
               val rows = Number.runtimeType[N] match {
                    case "Complex" => mat.getRows().map(vec => vec.toList
                         .map(e => e.asInstanceOf[Complex])
                         .map(e => number.Complex.valueOf(e.real.toDouble, e.imaginary.toDouble)).toArray)
                    case _ => mat.getRows().map(vec => vec.toList.map(e => number.Complex.valueOf(e.toDouble, 0)).toArray)
               }
               vector.ComplexMatrix.valueOf(rows.toArray)
          }

          //convert from MY matrix to matrixlib
          def convertOutL[N <: Number[N]: TypeTag](mat: VectorSet[N]): LibMatrix ={
               val rows = Number.runtimeType[N] match {
                    case "Complex" => mat.getRows().map(vec => vec.toList
                         .map(e => e.asInstanceOf[Complex])
                         .map(e => new ComplexNumber(e.real.toDouble, e.imaginary.toDouble)).toArray)
                    case _ => mat.getRows().map(vec => vec.toList.map(e => new ComplexNumber(e.toDouble, 0)).toArray)
               }
               new LibMatrix(rows.toArray)
          }
     }


     // ------------------------------------------------------------------------------

     object MatrixOps {

          object Id {

               def isSymmetric[N <: Number[N]: TypeTag](smat: SquareMatrix[N]): Boolean={
                    smat == smat.transpose()
               }

               def isHermitian[N <: Number[N]: TypeTag](smat: SquareMatrix[N]): Boolean={
                    smat == smat.conjugateTranspose()
               }

               def isUnitary[N <: Number[N]: TypeTag](smat: SquareMatrix[N]): Boolean ={
                    SquareMatrix.IDENTITY[N](smat) == (smat * smat.conjugateTranspose())
               }

               def isOrthogonal[N <: Number[N]: TypeTag](smat: SquareMatrix[N]): Boolean ={
                    SquareMatrix.IDENTITY[N](smat) == (smat * smat.transpose())
               }

               def isLowerTriangular[N <: Number[N]: TypeTag](mat: Matrix[N]): Boolean ={
                    for(r <- 0 until mat.numRows){
                         for(c <- 0 until mat.numCols){
                              if(c > r && mat.get(r, c) != 0)
                                   false
                         }
                    }
                    true
               }

               def isUpperTriangular[N <: Number[N]: TypeTag](mat: Matrix[N]): Boolean ={
                    for(r <- 0 until mat.numRows){
                         for(c <- 0 until mat.numCols){
                              if(c < r && mat.get(r, c) != 0)
                                   false
                         }
                    }
                    true
               }
          }

          object Trans {
               object HessenbergTransformer {
                    /**
                      * Computes the upper Hessenberg form of the matrix via similarity transforms
                      * based on Gauss-Jordan elimination
                      */
                    def makeHessenberg[N <: Number[N]: TypeTag](smat: SquareMatrix[N]): SquareMatrix[N] = {

                         var squareHessenMat: VectorSet[N] = smat.asInstanceOf[VectorSet[N]].copy()

                         for(r <- 0 until squareHessenMat.numRows){
                              //find max magnitude in the rth col below diagonal
                              var largest: Double = 0
                              var largestRow:Int = 0
                              for(i <- (r+1) until squareHessenMat.numRows){
                                   if(squareHessenMat.get(i, r).abs().toDouble > largest){
                                        largest = squareHessenMat.get(i, r).abs().toDouble
                                        largestRow = i
                                   }
                              }

                              if(largest != 0){
                                   //interchange rows largestRow and r+1
                                   squareHessenMat = Util.GenOps.swapRows(largestRow, r+1, squareHessenMat)
                                   //interchange cols largestRow and r+1 to make it a similarity transform
                                   squareHessenMat = Util.GenOps.swapCols(largestRow, r+1, squareHessenMat)

                                   for(i <- (r+2) until squareHessenMat.numRows){
                                        val mult: N = squareHessenMat.get(i, r) / squareHessenMat.get(r+1, r)

                                        //subtract mult * row r+1 from row i
                                        squareHessenMat = Util.GenOps.sumRows(i, r+1, mult, squareHessenMat)
                                        //add mult * col i to col r+1 to preserve similarity
                                        squareHessenMat = Util.GenOps.sumCols(r+1, i, mult, squareHessenMat)
                                   }
                              }
                         }
                         squareHessenMat.asInstanceOf[SquareMatrix[N]]
                    }
               }
          }
     }
}


