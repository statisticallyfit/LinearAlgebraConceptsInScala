package org.linalg.studies.linearalgebralibrary.factorization

import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
import org.linalg.studies.linearalgebralibrary.vector._


import org.apache.commons.lang3.StringUtils
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._



/**
  * TODO: find way to qr decompose complex matrices
  */
/**
  * Performs a QR decomposition on the given square matrix, writing a matrix as a product of
  * a unitary matrix Q and upper triangular matrix R
  *
  * @param mat the matrix whose QR factorization we wish to compute
  * @return the array representation {Q, R}
  *
  */
class QRDecomposition[N <: Number[N]: TypeTag](mat: Matrix[N]) extends Decomposition[N] {

     //todo support complex type
     require(Number.runtimeType[N] != "Complex", "QRDecomposition does not support Complex type yet.")

     private val m: Int = mat.numRows
     private val n: Int = mat.numCols
     private val rDiag: ListBuffer[N] = ListBuffer.fill[N](n)(Number.ZERO[N])
     private val decompCalc = decompose()
     private val Q = decompCalc._1  // square, unitary matrix m x m, where m = largest or numrows and numcols
     private val R = decompCalc._2 // non-square m x n, upper-triangular matrix, (same dim as original m x n matrix)


     def getQ(): Matrix[N] = Q

     def getR(): Matrix[N] = R

     def decompose(): (UnitaryMatrix[N], UpperTriangularMatrix[N]) ={
          givensQR()
     }

     /**
       * @return m x m orthogonal matrix Q and m x n upper triangular matrix R such that A = QR
       */
     private def givensQR(): (UnitaryMatrix[N], UpperTriangularMatrix[N]) ={
          val tempQ: SquareMatrix[N] = SquareMatrix.IDENTITY[N](m)
          val tempR: VectorSet[N] = mat.copy() //todo make matrix type at the outset

          for(i <- 1 until Math.min(m-1, n)){
               for(j <- (i+1) until m){
                    val csPair = givensParms(tempR.get(i, i), tempR.get(j, i))
                    val c = csPair._1
                    val s = csPair._2
                    givensMul(i, j, c, s, tempR)
                    givensMul(i, j, c, s, tempQ)
               }
          }
          (new UnitaryMatrix[N](tempQ), new UpperTriangularMatrix[N](tempR))
     }

     /**
       * @return givens parameters c,s to use in building givens rotation for matrix values xi, and xj
       */
     private def givensParms(xi:N, xj:N): (N, N) ={
          val zero: N = Number.ZERO[N]
          val one: N = Number.ONE[N]

          var c: N = zero
          var s: N = zero
          var t: N = zero

          if(xj == 0){
               c = one
          } else if (xj.abs() >= xi.abs()){
               t = xi / xj
               s = one / (one + t ^ 2).sqrt()
               c = s * t
          } else {
               t = xj / xi
               c = one / (one + t^2).sqrt()
               s = c * t
          }
          (c, s)
     }

     //todo when you make tempR matrix, fix matrix to be type matrix here
     private def givensMul(i:Int, j:Int, c:N, s:N, matrix: VectorSet[N]): Unit ={
          val a: Vector[N] = matrix.getRow(i)
          val b: Vector[N] = matrix.getRow(j)
          matrix.setRow(i, a.scale(c) + b.scale(s))
          matrix.setRow(j, a.scale(s.opposite()) + b.scale(c))
     }


     /*def decompose(): (Matrix[N], Matrix[N]) ={ //(UnitaryMatrix[N], UpperTriangularMatrix[N]) ={
          val QR: VectorSet[N] = mat.copy() //.asInstanceOf[Matrix[N]]


     }*/

     //since the decimals are too large/unwieldy for Fraction to approximate accurately, we go instead here from
     // showing rationals as reals in the complex form. Rationals stay as rationals, since that was the requirement,
     // but their decimal (real) values will not be accurate.
     override def toString: String ={
          if(Number.runtimeType[N] != "Complex")
               return "\nQ =" + Q.toString + "\nR =" + R.toString
          else {
               //---------------------------------------------------------------------------------------
               def mkString(colsStr: ListBuffer[ListBuffer[String]], numRows:Int, numCols:Int): String ={
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
               //---------------------------------------------------------------------------------------

               //else if complex, then we make the rationals display as reals.
               val strQ: ListBuffer[ListBuffer[String]] = Q.getColumns().map(col => col.toBuffer
                    .map(e => e.asInstanceOf[Complex].displayWithReals()))

               val strR: ListBuffer[ListBuffer[String]] = R.getColumns().map(col => col.toBuffer
                    .map(e => e.asInstanceOf[Complex].displayWithReals()))


               "\nQ =" + mkString(strQ, Q.numRows, Q.numCols) + "\nR =" + mkString(strR, R.numRows, R.numCols)
          }

     }

}




















/**
package org.linalg.studies.linearalgebralibrary


import org.apache.commons.lang3.StringUtils

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._



/**
  * TODO: find way to qr decompose complex matrices
  */
/**
  * Performs a QR decomposition on the given square matrix, writing a matrix as a product of
  * a unitary matrix Q and upper triangular matrix R
  *
  * @param mat the matrix whose QR factorization we wish to compute
  * @return the array representation {Q, R}
  *
  */
class QRDecomposition[N <: Number[N]: TypeTag](mat: Matrix[N]) extends Decomposition[N] {

     //todo support complex type
     require(Number.runtimeType[N] != "Complex", "QRDecomposition does not support Complex type yet.")

     private val m: Int = mat.numRows
     private val n: Int = mat.numCols
     private val rDiag: ListBuffer[N] = ListBuffer.fill[N](n)(Number.ZERO[N])
     private val decompCalc = decompose()
     private val Q = decompCalc._1  // square, unitary matrix m x m, where m = largest or numrows and numcols
     private val R = decompCalc._2 // non-square m x n, upper-triangular matrix, (same dim as original m x n matrix)


     def getQ(): Matrix[N] = Q

     def getR(): Matrix[N] = R


     def decompose(): (Matrix[N], Matrix[N]) ={ //(UnitaryMatrix[N], UpperTriangularMatrix[N]) ={
          val QR: VectorSet[N] = mat.copy() //.asInstanceOf[Matrix[N]]

          for(c <- 0 until n){ // c = k
               //compute 2-norm of kth col without under/overflow
               var the2Norm: Double = 0
               for(r <- c until m){ // r = i
                    the2Norm = Math.hypot(the2Norm, QR.get(r, c).toDouble)
                    if(Number.runtimeType[N] == "Complex"){
                         //change as ricled , get angle
                         the2Norm = Math.hypot(the2Norm,
                              Math.sqrt(QR.get(r, c).asInstanceOf[Complex].square().magnitude()))
                    }
               }

               if(the2Norm != 0.0){
                    //form the c-th householder vector
                    if(QR.get(c, c) < 0) the2Norm = -the2Norm

                    for(i <- c until m) {
                         QR.set(i, c)(QR.get(i, c) / Number.toNumber[N](the2Norm))
                    }
                    QR.set(c, c)(QR.get(c, c) + Number.ONE[N])

                    //Apply transformation to remaining columns
                    for(j <- (c+1) until n){
                         var s:N = Number.ZERO[N]

                         for(i <- c until m){
                              s += QR.get(i, c) * QR.get(i, j)
                         }
                         s = s.opposite() / QR.get(c,c)
                         for(i <- c until m){
                              QR.set(i, j)(QR.get(i, j) + (s * QR.get(i, c)))
                         }
                    }
               }
               rDiag(c) = Number.toNumber[N](the2Norm).opposite()
          }




          //getting the R matrix
          val tempR: Matrix[N] = Matrix.ZERO[N](n, n)
          for(r <- 0 until n){
               for(c <- 0 until n){
                    if(r < c) tempR.set(r, c)(QR.get(r, c))
                    else if(r == c) tempR.set(r, c)(rDiag(r))
                    else tempR.set(r, c)(Number.ZERO[N])
               }
          }


          //getting matrix Q
          val tempQ: Matrix[N] = Matrix.ZERO[N](m, n)
          val indicesK = (0 until n).reverse
          for(k <- indicesK){
               for(i <- 0 until m){
                    tempQ.set(i, k)(Number.ZERO[N])
               }
               tempQ.set(k, k)(Number.ONE[N])

               for(j <- k until n){
                    if(QR.get(k, k) != 0){
                         var s: N = Number.ZERO[N]
                         for(i <- k until m){
                              s += QR.get(i, k) * tempQ.get(i, j)
                         }
                         s = s.opposite() / QR.get(k, k)

                         for(i <- k until m){
                              tempQ.set(i, j)(tempQ.get(i, j) + (s * QR.get(i, k)))
                         }
                    }
               }
          }
          //(new UnitaryMatrix[N](tempQ), tempR)
          (tempQ, tempR)
     }

     //since the decimals are too large/unwieldy for Fraction to approximate accurately, we go instead here from
     // showing rationals as reals in the complex form. Rationals stay as rationals, since that was the requirement,
     // but their decimal (real) values will not be accurate.
     override def toString: String ={
          if(Number.runtimeType[N] != "Complex")
               return "\nQ =" + Q.toString + "\nR =" + R.toString
          else {
               //---------------------------------------------------------------------------------------
               def mkString(colsStr: ListBuffer[ListBuffer[String]], numRows:Int, numCols:Int): String ={
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
               //---------------------------------------------------------------------------------------

               //else if complex, then we make the rationals display as reals.
               val strQ: ListBuffer[ListBuffer[String]] = Q.getColumns().map(col => col.toBuffer
                    .map(e => e.asInstanceOf[Complex].displayWithReals()))

               val strR: ListBuffer[ListBuffer[String]] = R.getColumns().map(col => col.toBuffer
                    .map(e => e.asInstanceOf[Complex].displayWithReals()))


               "\nQ =" + mkString(strQ, Q.numRows, Q.numCols) + "\nR =" + mkString(strR, R.numRows, R.numCols)
          }

     }

}


  */