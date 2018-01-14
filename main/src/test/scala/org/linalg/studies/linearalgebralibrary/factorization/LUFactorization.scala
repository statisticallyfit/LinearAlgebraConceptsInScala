package org.linalg.studies.linearalgebralibrary.factorization

import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._


import matrixLib.exception._
import scala.reflect.runtime.universe._



// TODO find out if this needs square matrix tyupe and you can eliminate exceptions thrown.
/**
  * Computes and returns the LU decomposition of the matrix
  *
  * @param smat the matrix whose LU decomposition we are computing
  * @return the result of the LU decomposition {L,U}
  * @throws NotSquareException         the supplied matrix is not square
  * @throws NoLUDecompositionException the supplied square matrix does not admit an LU decomposition
  */
class LUFactorization[N <: Number[N]: TypeTag](smat: SquareMatrix[N]) extends Decomposition[N] {

     val size: Int = smat.numRows //same as numCols since matrix must be square

     private val decomposed: Option[(SquareMatrix[N], SquareMatrix[N])] = this.decompose()

     def getLowerTriangular(): Option[SquareMatrix[N]] ={
          if(decomposed.isDefined){
               Some(decomposed.get._1)
          } else {
               None
          }
     }

     def getUpperTriangular(): Option[SquareMatrix[N]] ={
          if(decomposed.isDefined){
               Some(decomposed.get._2)
          } else {
               None
          }


     def determinant(): N ={
          val lower = getLowerTriangular()
          var theProduct: N = lower.get(0,0) //using L not U because L is the one with det != 1
          for(i <- 1 until size){
               theProduct = theProduct * lower.get(i, i)
          }
          theProduct
     }

     /**
       * Computes and returns the LU decomposition of the matrix
       *
       * @return the result of the LU decomposition {L,U}
       * @throws NoLUDecompositionException the supplied square matrix does not admit an LU decomposition
       */
     def decompose(): Option[(SquareMatrix[N], SquareMatrix[N])] ={
          //no factorization if at (0,0) we have 0
          if(smat.get(0,0).isZero) throw new NoLUDecompositionException()

          val lower: SquareMatrix[N] = SquareMatrix.ZERO[N](smat)
          val upper: SquareMatrix[N] = SquareMatrix.ZERO[N](smat)

          //initialize L and U
          //initialize first row of U and first column of L
          for(c <- 0 until smat.numCols){
               upper.set(0, c)(smat.get(0, c) / smat.get(0, 0))
               lower.set(c, 0)(smat.get(c, 0))
          }

          for(n <- 1 until smat.numRows){
               //next computations are based on previously determined rows
               val prevL: Matrix[N] = Matrix.ZERO[N](smat.numRows - n, n)
               val prevU: Matrix[N] = Matrix.ZERO[N](smat.numRows - n, n)

               //compute previously determined row/col
               for(r <- n until smat.numRows){
                    for(c <- 0 until n){
                         prevL.set(r - n, c)(lower.get(r,c))
                         prevU.set(r - n, c)(upper.get(c,r))
                    }
               }

               //compute nth column of L
               for(r <- n until smat.numRows){
                    var theSum: N = Number.ZERO[N]
                    for(c <- 0 until n){
                         theSum = theSum + (prevL.get(r - n, c) * prevU.get(0, c))
                    }
                    lower.set(r, n)(smat.get(r, n) - theSum)
               }

               if(lower.get(n,n).isZero && n != (smat.numRows - 1)){
                    //then the factorization is not possible
                    throw new NoLUDecompositionException()
               }

               upper.set(n,n)(Number.ONE[N])
               if(n != (smat.numRows - 1)){
                    //compute the nth row of U, right of the diagonal
                    for(c <- (n+1) until smat.numCols){
                         var theSum: N = Number.ZERO[N]
                         for(k <- 0 until n){
                              theSum = theSum + (prevL.get(0, k) * prevU.get(c - n, k))
                         }
                         upper.set(n,c)((smat.get(n, c) - theSum) / lower.get(n,n))
                    }
               }
          }
          Some(lower, upper)
     }

}
