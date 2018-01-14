package matrixLib;

/**
 * Implementation of vectors (column matrices)
 * @author Bryan Cuccioli
 */

import matrixLib.exception.*;

public class Vector extends Matrix {
	
	/**
	 * Constructs an empty vector of size n
	 * @param n the size of the vector
	 * @throws DimensionMismatchException the given dimension is invalid
	 */
	public Vector(int n) throws DimensionMismatchException {
		
		super(n, 1);
	}

	/**
	 * Constructs a wrapping vector for entries
	 * @param entries the underlying data for the new vector
	 */
	public Vector(ComplexNumber[] entries) {
		
		super(parse(entries)); // make it look like a two dimensional array
	}
	
	/**
	 * Constructs a wrapping vector for entries
	 * @param entries the underlying data for the new vector
	 */
	public Vector(double[] entries) {
	
		super(parse(entries)); // make it look like a two dimensional array
	}
	
	/**
	 * Wraps a 1d array to a 2d column array, for use in several constructors
	 * @param array the array to wrap
	 * @return the 2d column array with the given entries
	 */
	private static ComplexNumber[][] parse(ComplexNumber[] array) {
		
		ComplexNumber[][] ret = new ComplexNumber[array.length][1];
		for (int i = 0; i < array.length; i++) {
			// each element gets its own row
			ret[i][0] = array[i];
		}
		
		return ret;
	}
	
	/**
	 * Wraps a 1d array to a 2d column array, for use in several constructors
	 * @param array the array to wrap
	 * @return the 2d column array with the given entries
	 */
	private static double[][] parse(double[] array) {
		
		double[][] ret = new double[array.length][1];
		for (int i = 0; i < array.length; i++) {
			// each element gets its own row
			ret[i][0] = array[i];
		}
		
		return ret;
	}
	
	/**
	 * Gets a value at a certain coordinate
	 * @param i the coordinate whose value we wish to retrieve
	 * @return the value at the ith coordinate in the vector
	 * @throws ArrayIndexOutOfBoundsException tried to access an invalid coordinate
	 */
	public ComplexNumber getAt(int i) throws IndexOutOfBoundsException {
		
		if (i < 0 || i >= dim()) {
			throw new IndexOutOfBoundsException();
		}
		
		return super.getAt(i, 0);
	}
	
	/**
	 * Sets the element at a given coordinate to something
	 * @param coord the coordinate to set the value at
	 * @param val the value to set at the given coordinate
	 * @throws ArrayIndexOutOfBoundsException tried to access an invalid coordinate
	 */
	public void set(int coord, ComplexNumber val) throws IndexOutOfBoundsException {
		
		if (coord < 0 || coord >= dim()) {
			throw new IndexOutOfBoundsException();
		}
		
		super.set(coord, 0, val);
	}
	
	/**
	 * Returns the dimension of the vector space that the vector resides in
	 * @return the number of coordinates of the vector
	 */
	public int dim() {
		
		// the dimension is the number of rows if considered as a matrix
		return super.rows();
	}
	
	/**
	 * Adds two vectors
	 * @param v the vector to add to this one
	 * @return the vector sum of this vector and v
	 * @throws DimensionMismatchException the vectors to add do not have matching dimension
	 */
	public Vector add(Vector v) throws DimensionMismatchException {
		
		if (this.dim() != v.dim()) { // need same dimensions to add
			throw new DimensionMismatchException("incompatible dimensions");
		}
		
		// add vectors elementwise
		ComplexNumber[] sum = new ComplexNumber[dim()];
		for (int i = 0; i < dim(); i++) {
			sum[i] = getAt(i).add(v.getAt(i));
		}
		return new Vector(sum);
	}

	/**
	 * Subtracts a vector from this one
	 * @param v the vector to subtract
	 * @return the vector difference of this vector and v
	 * @throws DimensionMismatchException the vectors do not have matching dimension
	 */
	public Vector subtract(Vector v) throws DimensionMismatchException {
		
		if (this.dim() != v.dim()) { // need same dimensions to subtract
			throw new DimensionMismatchException("incompatible dimensions");
		}
		
		// subtract vectors elementwise
		ComplexNumber[] diff = new ComplexNumber[dim()];
		for (int i = 0; i < dim(); i++) {
			diff[i] = getAt(i).subtract(v.getAt(i));
		}
		return new Vector(diff);
	}
	
	/**
	 * Multiplies a vector by a scalar by multiplying each element by that scalar
	 * @param s the scalar by which to multiply the vector
	 * @return the newly scaled vector
	 */
	public Vector multiply(ComplexNumber s) {
		
		ComplexNumber[] scaled = new ComplexNumber[dim()];
		for (int i = 0; i < dim(); i++) { // multiply elementwise
			scaled[i] = getAt(i).multiply(s);
		}
		return new Vector(scaled);
	}
	
	/**
	 * Multiplies a vector by a scalar by multiplying each element by that scalar
	 * @param s the scalar by which to multiply the vector
	 * @return the newly scaled vector
	 */
	public Vector multiply(double s) {
		
		ComplexNumber[] scaled = new ComplexNumber[dim()];
		for (int i = 0; i < dim(); i++) { // multiply elementwise
			scaled[i] = getAt(i).multiply(s);
		}
		return new Vector(scaled);
	}
	
	/**
	 * Compute the Hermitian inner product (dot product) of two vectors
	 * @param v the vector to dot against this
	 * @return the inner product of the two vectors
	 * @throws DimensionMismatchException the vectors do not have matching dimension 
	 */
	public ComplexNumber dot(Vector v) throws DimensionMismatchException {
		
		if (this.dim() != v.dim()) { // need matching dimensions for inner product
			 new DimensionMismatchException();
		}
		
		ComplexNumber dotprod = new ComplexNumber(0, 0);
		for (int i = 0; i < dim(); i++) { // compute Hermitian inner product
			dotprod = dotprod.add(this.getAt(i).multiply(v.getAt(i).conjugate()));
		}
		return dotprod;
	}
	
	/**
	 * Compute the cross product between two vectors
	 * @param v The vector to cross this with
	 * @return The cross product between this and v, this x v
	 * @throws DimensionMismatchException the vectors are not in R^3 or C^3
	 */
	public Vector cross(Vector v) throws DimensionMismatchException {
		
		// cross product is only defined for vectors in F^3, e.g. R^3
		if (this.dim() != 3 || v.dim() != 3) {
			 new DimensionMismatchException();
		}
		
		// apply the cross product formula
		ComplexNumber[] coords = new ComplexNumber[3];		
		coords[0] = getAt(1).multiply(v.getAt(2)).subtract(getAt(2).multiply(v.getAt(1)));
		coords[1] = getAt(2).multiply(v.getAt(0)).subtract(getAt(0).multiply(v.getAt(2)));
		coords[2] = getAt(0).multiply(v.getAt(1)).subtract(getAt(1).multiply(v.getAt(0)));
		
		return new Vector(coords);
	}
	
	/**
	 * Return the vector that is the projection of this onto a certain vector
	 * @param onto the vector onto which we project this
	 * @return the projection proj_onto(this)
	 * @throws DimensionMismatchException the vectors do not have matching dimension
	 */
	public Vector proj(Vector onto) throws DimensionMismatchException {
		
		if (this.dim() != onto.dim()) {
			 new DimensionMismatchException();
		}
		
		ComplexNumber factor = onto.dot(this).divide(onto.dot(onto)); // scaling factor
		ComplexNumber[] entries = new ComplexNumber[dim()];
		
		for (int i = 0; i < dim(); i++) { // multiply each entry by scaling factor
			entries[i] = onto.getAt(i).multiply(factor);
		}
		
		return new Vector(entries);
	}
	
	/**
	 * Returns the unit vector pointing in the same direction as this vector
	 * @return the unit vector pointing in the same direction as this vector
	 */
	public Vector normalize() {
		
		// compute the complex norm of this vector
		ComplexNumber norm = new ComplexNumber(0, 0);
		for (int i = 0; i < dim(); i++) {
			norm = norm.add(getAt(i).multiply(getAt(i).conjugate()));
		}
		norm = norm.sqrt();
		// and divide each element by this norm
		ComplexNumber[] entries = new ComplexNumber[dim()];
		for (int i = 0; i < dim(); i++) {
			entries[i] = getAt(i).divide(norm);
		}
		
		return new Vector(entries);
	}
	
	/**
	 * Generates a unitary matrix whose first column is this vector
	 * @return a unitary matrix whose first column is this vector 
	 */
	public Matrix generateUnitaryMatrix() {
		
		Vector[] overspan = new Vector[this.dim()+1];
		overspan[0] = this.normalize();
		ComplexNumber[] oscontent = new ComplexNumber[this.dim()];
		oscontent[0] = new ComplexNumber(1,0);
		
		for (int i = 1; i < this.dim(); i++) { // initialize oscontent to <1,0,...,0>
			oscontent[i] = new ComplexNumber(0,0);
		}
		
		// generate vectors that do better than span the whole vector space
		for (int i = 1; i <= this.dim(); i++) {
			overspan[i] = new Vector(oscontent);
			if (i != this.dim()) {
				oscontent[i] = oscontent[i-1];
			}
			oscontent[i-1] = new ComplexNumber(0,0);
		}
		
		// choose a maximal subset of independent vectors
		Matrix allvectors = new Matrix(overspan);
		Matrix rref = allvectors.rref();
		Vector[] basis = new Vector[this.dim()];
		int vec_pos = 0;
		// the first n pivot columns specify the maximal independent subset
		for (int i = 0; i < this.dim(); i++) {
			for (int j = 0; j <= this.dim(); j++) {
				if (!rref.getAt(i,j).isZero()) { // found a new pivot
					basis[vec_pos++] = allvectors.getVector(j);
					break;
				}
			}
		}
		
		// apply Gram-Schmidt to orthogonalize the vectors
		return (new Matrix(basis)).orthonormalize();
	}
	
	/**
	 * Determine the elementary reflector associated with this vector
	 * @return the elementary reflector associated with this vector 
	 */
	public Matrix reflector() {
		
		double factor = 2.0/Math.pow(Norm.pnorm(this, 2), 2); // scaling factor
		ComplexNumber[][] ref = new ComplexNumber[this.dim()][this.dim()];
		
		// compute the identity matrix minus the product of the vectors scaled
		for (int i = 0; i < this.dim(); i++) {
			for (int j = 0; j < this.dim(); j++) {
				ref[i][j] = (i==j) ? new ComplexNumber(1,0) : new ComplexNumber(0,0);
				ref[i][j] = ref[i][j].subtract(getAt(i).multiply(getAt(j)).multiply(factor));
			}
		}
		
		return new Matrix(ref);
	}
	
	/**
	 * Tells whether this is the 0 vector (all coordinates are 0)
	 * @return a boolean value indicating whether this is the 0 vector
	 */
	public boolean isZero() {
		
		for (int i = 0; i < dim(); i++) {
			if (!getAt(i).isZero()) {
				return false; // found a nonzero one
			}
		}
		return true; // all of the coordinates were 0
	}
	
	/**
	 * Returns a string representation of this vector <a, b, ...>
	 * @return a string representation <a, b, ...>
	 */
	public String toString() {
		
		String str = "<";
		for (int i = 0; i < this.dim(); i++) {
			str += this.getAt(i);
			if (i != this.dim() - 1) str += ", ";
		}
		str += ">";
		
		return str;
	}
}
