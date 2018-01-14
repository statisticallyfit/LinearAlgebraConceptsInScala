package org.linalg.studies.linearalgebralibrary.factorization

import org.linalg.studies.linearalgebralibrary.util._
import org.linalg.studies.linearalgebralibrary.theory._
import org.linalg.studies.linearalgebralibrary.number._
import org.linalg.studies.linearalgebralibrary.matrix._
/*


/**
  *
  */

//todo when you instantiate squarematrix make mat a squarematrix
class Eigen[N <: Number[N]](squareMatrix: SquareMatrix[N]) extends Decomposition[N] {
     //singular values
     //eigenvals, eigenvectors

     private val decomp = decompose()
     private val eigenvalues: VectorSet[N] = decomp._1
     private val eigenvectors: VectorSet[N] = decomp._2

     def decompose(): (VectorSet[N], VectorSet[N]) ={
          (eigenValues(), eigenVectors())
     }

     //todo fix type must be vector array
     def eigenVectors(): VectorSet[N] = VectorSet.ZERO[N](2, 3)

     def eigenValues(): VectorSet[N] ={

		 VectorSet.ZERO[N](1, 1)

          /**
            * Matrix curr = Pattern.hessenberg(m); // makes it converge sooner
		ComplexNumber[] evals = new ComplexNumber[m.rows()];
		int pos = 0; // where we are in the evals array
		int size = m.rows(); // the size of the current array

		while (size > 2) { // once the matrix is 2x2, can just use the explicit form
			Matrix shift = Pattern.diag(curr.getAt(size-1,size-1),size);
			Matrix[] qr = Factorization.QRDecompose(curr.subtract(shift));
			curr = qr[1].multiply(qr[0]).add(shift); // similarity transform, preserves spectrum

			int form = getForm(curr);
			if (form == 1) { // the lower right element is an eigenvalue
				size--; // we will operate on the upper left block of size one less
				evals[pos++] = curr.getAt(size, size);
			}
			else if (form == 2) {
				size -= 2; // operate on the upper left block of size two less
				// apply formula for eigenvalues of a 2x2 matrix
				ComplexNumber left = curr.getAt(size,size).add(curr.getAt(size+1,size+1));
				ComplexNumber leftprime = curr.getAt(size,size).subtract(curr.getAt(size+1,size+1));
				ComplexNumber right = curr.getAt(size,size+1).multiply(curr.getAt(size+1,size)).multiply(4).add(leftprime.multiply(leftprime)).sqrt();
				evals[pos++] = left.add(right).multiply(.5);
				evals[pos++] = left.subtract(right).multiply(.5);
			}
			if (size == 0) { // we have fully reduced the matrix
				return evals;
			}
			if (form != 0) { // we found {1|2} eigenvalues, so reduce
				// build the new matrix to operate on if an eval was found
				ComplexNumber[][] newmat = new ComplexNumber[size][size];
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						newmat[i][j] = curr.getAt(i,j);
					}
				}
				curr = new Matrix(newmat);
			}
		}

		if (size == 1) {
			evals[pos++] = curr.getAt(0,0); // there is a 1x1 matrix remaining
		}
		else {
			// size=2 in this branch; apply explicit 2x2 formula
			ComplexNumber left = curr.getAt(0,0).add(curr.getAt(1,1));
			ComplexNumber leftprime = curr.getAt(0,0).subtract(curr.getAt(1,1));
			ComplexNumber right = curr.getAt(0,1).multiply(curr.getAt(1,0)).multiply(4).add(leftprime.multiply(leftprime)).sqrt();
			evals[pos++] = left.add(right).multiply(.5);
			evals[pos++] = left.subtract(right).multiply(.5);
		}

		return evals;
	}
            */
     }
}
*/
