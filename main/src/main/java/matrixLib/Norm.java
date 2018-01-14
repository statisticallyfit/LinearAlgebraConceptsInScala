package matrixLib;

/**
 * Library of routines for computing matrix norms
 * @author Bryan Cuccioli
 */

public class Norm {

	/**
	 * Computes the p-norm of the matrix
	 * @param m the matrix whose norm is to be computed
	 * @param p specifies which norm is to be computed
	 * @return the p-norm of the matrix
	 */
	public static double pnorm(Matrix m, double p) {
		
		if (p == 1) {
			// interface with 1-norm, which is a different procedure
			return oneNorm(m);
		}
		
		double sum = 0;
		
		for (int i = 0; i < m.rows(); i++) {
			for (int j = 0; j < m.cols(); j++) {
				if (p == 2) {
					// use as few square roots as possible for speed/numerical stability
					sum += Math.pow(m.getAt(i,j).Re(), p) + Math.pow(m.getAt(i,j).Im(), p);
				}
				else {
					sum += Math.pow(m.getAt(i, j).abs(), p);
				}
			}
		}

		return Math.pow(sum, 1.0/((double)p));
	}
	
	/**
	 * Computes the one norm of the matrix, the largest column sum of absolute value
	 * @param m the matrix whose one norm is to be computed
	 * @return the one norm of the given matrix
	 */
	public static double oneNorm(Matrix m) {
		
		double largest = 0;
		
		for (int i = 0; i < m.cols(); i++) {
			double sum = 0;
			for (int j = 0; j < m.rows(); j++) {
				sum += m.getAt(j, i).abs(); // compute the column sum
			}
			if (sum > largest) {
				largest = sum; // found a new largest column sum
			}
		}
		
		return largest;
	}
	
	/**
	 * Computes the infinity norm of this matrix, the largest row sum of absolute value
	 * @param m the matrix whose infinity norm is to be computed
	 * @return the infinity norm of the matrix
	 */
	public static double infinityNorm(Matrix m) {
		
		double largest = 0;
		
		for (int i = 0; i < m.rows(); i++) {
			double sum = 0;
			for (int j = 0; j < m.cols(); j++) {
				sum += m.getAt(i, j).abs(); // compute the row sum
			}
			if (sum > largest) {
				largest = sum; // found a new row sum
			}
		}
		
		return largest;
	}
	
	/**
	 * Computes the Frobenius norm (2-norm) of the matrix
	 * @param m the matrix whose Frobenius norm we are computing
	 * @return the Frobenius norm of the matrix
	 */
	public static double frobeniusNorm(Matrix m) {
		
		return pnorm(m, 2);
	}
	
	/**
	 * Computes the spectral norm, the largest square root of an eigenvalue
	 * @param m the matrix whose spectral norm is to be computed
	 * @return the spectral norm of the matrix
	 */
	public static ComplexNumber spectralNorm(Matrix m) {
		
		ComplexNumber[] eigenvalues = SquareMatrixOps.eigenvalues(m.conjugateTranspose().multiply(m));
		ComplexNumber largest = new ComplexNumber(0, 0);
		
		for (ComplexNumber eval : eigenvalues) {
			if (eval.sqrt().abs() > largest.abs()) {
				largest = eval.sqrt();
			}
		}
		
		return largest;
	}
}
