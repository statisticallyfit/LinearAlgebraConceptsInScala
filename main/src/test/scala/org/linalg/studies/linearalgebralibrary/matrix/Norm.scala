package org.linalg.studies.linearalgebralibrary.matrix

import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
import org.linalg.studies.linearalgebralibrary.vector._
import org.linalg.studies.linearalgebralibrary.theory._
/**
  *
  */
class Norm[N <: Number[N]](mat: Matrix[N])

object Norm {
     /** Computes the Frobenius norm (2-norm) of the matrix */
     def frobeniusNorm[N <: Number[N]](mat: Matrix[N]): Double = pnorm(mat, 2)

     /** Computes the infinity norm of this matrix, the largest row sum of absolute value */
     def infinityNorm[N <: Number[N]](mat: Matrix[N]): Double ={
          mat.getRows().map(row => row.toList.map(_.abs().toDouble).sum).max
     }

     /** Computes the one norm of the matrix, the largest column sum of absolute value */
     def oneNorm[N <: Number[N]](mat: Matrix[N]): Double ={
          mat.getColumns().map(col => col.toList.map(_.abs().toDouble).sum).max
     }

     /**
       * Computes the p-norm of the matrix
       *
       * @param mat the matrix whose norm is to be computed
       * @param p specifies which norm is to be computed
       * @return the p-norm of the matrix
       */
     def pnorm[N <: Number[N]](mat: Matrix[N], p: Int): Double ={

          if(p == 1) return oneNorm(mat)

          val theSum: Double = mat.getColumns().map(col => col.toList.map(e => Math.pow(e.abs().toDouble, p)).sum)
               .sum
          Math.pow(theSum, 1.0/p)

          //todo later test the functional way above against this
          /*var sum: Double = 0.0
          for(r <- 0 until mat.numRows){
               for(c <- 0 until mat.numCols){
                    sum += Math.pow(mat.get(r,c).abs().toDouble, p)
               }
          }
          Math.pow(sum, 1.0/p)*/
     }

     /** Computes the spectral norm, the largest square root of an eigenvalue */
     def spectralNorm[N <: Number[N]](mat: Matrix[N]): Double ={
          return 0
          /*ComplexNumber[] eigenvalues = SquareMatrixOps.eigenvalues(m.conjugateTranspose().multiply(m));
          ComplexNumber largest = new ComplexNumber(0, 0);

          for (ComplexNumber eval : eigenvalues) {
               if (eval.sqrt().abs() > largest.abs()) {
                    largest = eval.sqrt();
               }
          }

          return largest;*/
     }
}
