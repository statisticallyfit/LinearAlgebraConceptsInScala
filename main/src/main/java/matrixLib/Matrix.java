package matrixLib;

import java.util.LinkedList;
import matrixLib.exception.*;

/**
 * Represents a matrix over the complex numbers
 * @author Bryan Cuccioli
 */
public class Matrix {

    private ComplexNumber[][] matrix;
    private int rows, cols;

    /**
     * Constructs the identity matrix of a given size
     * @param n the number of rows and columns in this identity matrix
     * @throws DimensionMismatchException the given matrix size is less than one
     */
    public Matrix(int n) throws DimensionMismatchException {

        if (n < 1) { // tried to give invalid size
            throw new DimensionMismatchException("matrix must be at least 1 by 1");
        }

        matrix = new ComplexNumber[n][n];
        rows = cols = n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = new ComplexNumber((i == j) ? 1 : 0, 0);
            }
        }
    }

    /**
     * Constructs the matrix that wraps the given array
     * @param mat the data to go in the matrix
     * @throws DimensionMismatchException the given array is not rectangular
     */
    public Matrix(double[][] mat) throws DimensionMismatchException {

        // make sure the given array has at least some data
        if (mat.length == 0 || mat[0].length == 0) {
            throw new DimensionMismatchException("no data in array");
        }

        matrix = new ComplexNumber[mat.length][mat[0].length];

        for (int i = 0; i < mat.length; i++) {
            if (mat[0].length != mat[i].length) { // jagged array found
                throw new DimensionMismatchException("jagged array");
            }
            for (int j = 0; j < mat[0].length; j++) {
                matrix[i][j] = new ComplexNumber(mat[i][j], 0);
            }
        }

        rows = mat.length;
        cols = mat[0].length; // the number of columns in the matrix
    }

    /**
     * Constructs the matrix that wraps the array mat[][]
     * @param mat the data to go in the matrix
     * @throws DimensionMismatchException the supplied array does not have valid dimensions
     */
    public Matrix(ComplexNumber[][] mat) throws DimensionMismatchException {

        // make sure the given array has at least some data
        if (mat.length == 0 || mat[0].length == 0) {
            throw new DimensionMismatchException("no data in array");
        }

        matrix = new ComplexNumber[mat.length][mat[0].length];

        for (int i = 0; i < mat.length; i++) {
            if (mat[0].length != mat[i].length) { // jagged array
                throw new DimensionMismatchException("jagged array");
            }
            for (int j = 0; j < mat[0].length; j++) {
                matrix[i][j] = mat[i][j];
            }
        }

        rows = mat.length;
        cols = mat[0].length; // the number of columns in the matrix
    }

    /**
     * Create a new matrix of the given dimensions populated by zeros
     * @param r the number of rows in the matrix
     * @param c the number of columns in the matrix
     * @throws DimensionMismatchException invalid number of rows or columns
     */
    public Matrix(int r, int c) {

        if (r < 1 || c < 1) { // make sure there are some rows and columns
            throw new DimensionMismatchException("invalid row/column configuration");
        }

        matrix = new ComplexNumber[r][c];
        this.rows = r;
        this.cols = c;

        // build the zero matrix
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                matrix[i][j] = new ComplexNumber(0, 0);
            }
        }
    }

    /**
     * Constructs a matrix from a collection of vectors
     * @param vectors the vectors to create the matrix from
     * @s DimensionMismatchException the vectors do not all have the same dimension
     */
    public Matrix(Vector[] vectors) throws DimensionMismatchException {

        this.rows = vectors[0].dim();
        this.cols = vectors.length;
        matrix = new ComplexNumber[rows][cols];

        for (int j = 0; j < cols; j++) {
            if (j != 0 && vectors[j].dim() != vectors[j-1].dim()) {
                // the vectors did not all have the same number of coordinates
                throw new DimensionMismatchException("jagged set of vectors");
            }
            for (int i = 0; i < rows; i++) {
                matrix[i][j] = vectors[j].getAt(i);
            }
        }
    }

    /**
     * Retrieves the nth vector from the matrix
     * @param n the column/vector to get from the matrix
     * @return the nth vector from the matrix
     * @throws ArrayIndexOutOfBoundsException invalid column number given
     */
    public Vector getVector(int n) {

        // check validity of column number
        if (n < 0 || n >= cols) {
            throw new ArrayIndexOutOfBoundsException("invalid column number");
        }

        ComplexNumber[] entries = new ComplexNumber[rows()];
        // populate the vector with that matrix column
        for (int i = 0; i < rows(); i++) {
            entries[i] = matrix[i][n];
        }

        return new Vector(entries);
    }

    /**
     * Gets the element at location (r,c) in the matrix
     * @param r the row to retrieve the element from
     * @param c the column to retrieve the element from
     * @return the element in the matrix at (r,c)
     * @throws ArrayIndexOutOfBoundsException trying to access an element out of the bounds of the matrix
     */
    public ComplexNumber getAt(int r, int c) throws ArrayIndexOutOfBoundsException {

        if (r >= rows() || c >= cols() || r < 0 || c < 0) {
            throw new ArrayIndexOutOfBoundsException("tried to access outside of the matrix");
        }

        return matrix[r][c];
    }

    /**
     * Sets the element at (r, c) to something
     * @param r the row of the element to set
     * @param c the column of the element to set
     * @param val the value to set (r, c) to
     * @throws ArrayIndexOutOfBoundsException trying to access an element out of the bounds of the matrix
     */
    public void set(int r, int c, ComplexNumber val) throws ArrayIndexOutOfBoundsException {

        if (r >= rows() || c >= cols() || c < 0 || r < 0) {
            throw new ArrayIndexOutOfBoundsException("tried to access outside of the matrix");
        }

        matrix[r][c] = val;
    }

    /**
     * Returns the matrix transpose of this matrix (reflected about the diagonal)
     * @return the transpsoe of this matrix
     */
    public Matrix transpose() {

        ComplexNumber[][] trans = new ComplexNumber[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                trans[i][j] = matrix[j][i];
            }
        }

        return new Matrix(trans);
    }

    /**
     * Returns the conjugate transpose of this matrix (transposed and conjugated)
     * @return the matrix that is the conjugate transpose of this matrix
     */
    public Matrix conjugateTranspose() {

        ComplexNumber[][] ct = new ComplexNumber[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                ct[i][j] = matrix[j][i].conjugate();
            }
        }

        return new Matrix(ct);
    }

    /**
     * Returns the number of rows in the matrix
     * @return the number of rows
     */
    public int rows() {
        return rows;
    }

    /**
     * Returns the number of columns in the matrix
     * @return the number of columns
     */
    public int cols() {
        return cols;
    }

    /**
     * Tells whether the matrix is composed entirely of real numbers
     * @return boolean value indicating whether the matrix is real
     */
    public boolean isReal() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j].Im() != 0) {
                    return false; // complex entry found
                }
            }
        }
        return true; // didn't find any complex entries
    }

    /**
     * Returns the result of multiplying this matrix by m
     * @param m the matrix to multiply this one by
     * @return the matrix product this*m
     * @throws DimensionMismatchException the matrices do not have the right dimensions to be multiplied
     */
    public Matrix multiply(Matrix m) throws DimensionMismatchException {

        // can only multiply matrices of dimension nxm by mxp
        if (cols != m.rows()) {
            throw new DimensionMismatchException("incompatible dimensions for multiplying");
        }

        // otherwise compute the matrix product in the standard way
        ComplexNumber[][] prod = new ComplexNumber[rows()][m.cols()];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.cols(); j++) {
                prod[i][j] = new ComplexNumber(0,0);
                for (int k = 0; k < cols; k++) {
                    prod[i][j] = prod[i][j].add(matrix[i][k].multiply(m.getAt(k, j)));
                }
            }
        }

        return new Matrix(prod);
    }

    /**
     * Multiplies this matrix by the given vector
     * @param v the vector by which to multiply the matrix
     * @return the product of this matrix and the given vector
     * @throws DimensionMismatchException the vector is not in the domain of this matrix
     */
    public Vector multiply(Vector v) throws DimensionMismatchException {

        if (this.cols() != v.dim()) {
            throw new DimensionMismatchException();
        }

        // matrix multiplication logic for a single column
        ComplexNumber[] prod = new ComplexNumber[this.rows()];
        for (int i = 0; i < rows(); i++) {
            prod[i] = new ComplexNumber(0,0); // initialize for addition
            for (int j = 0; j < cols(); j++) {
                prod[i] = prod[i].add(matrix[i][j].multiply(v.getAt(j)));
            }
        }

        return new Vector(prod);
    }

    /**
     * Adds this matrix to the given matrix
     * @param m the matrix to add to this one
     * @return the matrix sum this+m
     * @throws DimensionMismatchException the matrices do not have matching dimensions
     */
    public Matrix add(Matrix m) throws DimensionMismatchException {

        if (m.rows() != rows || m.cols() != cols) {
            throw new DimensionMismatchException();
        }

        // add matrices elementwise
        ComplexNumber[][] sum = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum[i][j] = matrix[i][j].add(m.getAt(i,j));
            }
        }

        return new Matrix(sum);
    }

    /**
     * Subtracts the given matrix from this one
     * @param m the matrix to subtract from this one
     * @return the difference between this matrix and the given
     * @throws DimensionMismatchException the matrices do not have matching dimensions
     */
    public Matrix subtract(Matrix m) throws DimensionMismatchException {

        if (m.rows() != rows || m.cols() != cols) {
            throw new DimensionMismatchException();
        }

        // subtract matrices elementwise
        ComplexNumber[][] diff = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                diff[i][j] = matrix[i][j].subtract(m.getAt(i,j));
            }
        }

        return new Matrix(diff);
    }

    /**
     * Multiplies each element of the matrix by a complex number
     * @param factor the scalar to multiply the matrix by
     * @return the matrix with each element multiplied by the scalar
     */
    public Matrix multiply(ComplexNumber factor) {

        // multiply each element by factor
        ComplexNumber[][] scaled = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                scaled[i][j] = matrix[i][j].multiply(factor);
            }
        }

        return new Matrix(scaled);
    }

    /**
     * Multiplies each element of the matrix by a real scalar
     * @param factor the scalar to multiply the matrix by
     * @return the matrix with each element multiplied by the scalar
     */
    public Matrix multiply(double factor) {

        return multiply(new ComplexNumber(factor, 0));
    }

    /**
     * Computes an orthonormal basis for the column space of the matrix
     * using the numerically stable modified Gram-Schmidt procedure
     * @return the matrix whose columns are an orthonormal basis for the column space of the matrix
     */
    public Matrix orthonormalize() {

        // store the columns of the matrices as an array of vectors
        Vector[] result = new Vector[cols()];
        for (int j = 0; j < cols(); j++) {
            result[j] = this.getVector(j);
        }

        for (int j = 0; j < cols(); j++) {
            result[j] = result[j].normalize();
            // make each subsequent vector span the remaining subspace
            for (int i = j+1; i < cols(); i++) {
                result[i] = result[i].subtract(result[j].multiply(result[j].dot(result[i])));
            }
        }

        return new Matrix(result); // columns are now orthonormalized
    }

    /**
     * Gets the underlying data array for this matrix
     * @return the underlying data array for this matrix
     */
    protected ComplexNumber[][] getData() {

        return this.matrix;
    }

    /**
     * Gets the canonical basis for the image of the matrix
     * @return the vectors forming a basis for the image of the matrix
     */
    public LinkedList<Vector> imageBasis() {

        Matrix rref = this.rref();
        LinkedList<Vector> basis = new LinkedList<Vector>();

        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                if (!rref.getAt(i,j).isZero()) {
                    basis.add(getVector(j)); // the basis is formed by the pivot columns
                    break; // move on to the next row and find the next pivot
                }
            }
        }

        return basis;
    }

    /**
     * Gets the singular values of the matrix, which are the square roots of the
     * eigenvalues of the matrix A^H A
     * @return an array of singular values of the matrix
     */
    public ComplexNumber[] singularValues() {

        ComplexNumber[] sv = SquareMatrixOps.eigenvalues(this.conjugateTranspose().multiply(this));

        for (int i = 0; i < sv.length; i++) {
            sv[i] = sv[i].sqrt();
        }

        return sv;
    }

    /**
     * Helper method for rref(), used for computing either the rref or the determinant
     * @param type 0 to tell it to compute rref, 1 to compute determinant
     * @return the rref form of the matrix, or a matrix holding the determinant at the (0,0) element
     */
    protected Matrix rref(int type) {

        Matrix rref = new Matrix(matrix);
        ComplexNumber det = new ComplexNumber(1,0);

        int lead = 0;
        for (int r = 0; r < rows(); r++) { // compute for each row
            if (lead >= cols()) {
                break;
            }
            int i = r;
            while (rref.getAt(i, lead).isZero()) { // find the pivot element
                i++;
                if (i == rows()) {
                    i = r;
                    lead++;
                    if (lead == cols()) { // we found the last pivot
                        // returning; setup determinant computation if necessary
                        if (type == 1) {
                            if (Pattern.isIdentity(rref)) {
                                rref.set(0, 0, det);
                            }
                            else {
                                rref.set(0, 0, new ComplexNumber(0, 0));
                            }
                        }
                        return rref;
                    }
                }
            }

            // swap rows i and r
            if (i != r) {
                for (int a = 0; a < cols(); a++) {
                    ComplexNumber temp = rref.getAt(i, a);
                    rref.set(i, a, rref.getAt(r, a));
                    rref.set(r, a, temp);
                }
                // swapping rows negates the determinant
                det = det.multiply(-1);
            }

            // divide row r by rref[r][lead]
            ComplexNumber div = rref.getAt(r, lead);
            for (int a = 0; a < cols(); a++) {
                rref.set(r, a, rref.getAt(r, a).divide(div));
            }
            // scaling the matrix scales the determinant
            det = det.multiply(div);

            for (int j = 0; j < rows(); j++) { // back-substitute upwards
                if (j != r) {
                    // subtract row r * -rref[j][lead] from row j
                    // this has no effect on the determinant
                    ComplexNumber c = rref.getAt(j, lead);
                    for (int a = 0; a < cols(); a++) {
                        rref.set(j, a, rref.getAt(j, a).subtract(rref.getAt(r, a).multiply(c)));
                    }
                }
            }
            lead++; // now looking for a pivot further right
        }

        if (type == 0) { // just wanted the simple rref
            return rref;
        }
        else {
            // if we didn't row reduce to identity, the matrix is singular
            if (Pattern.isIdentity(rref)) {
                rref.set(0, 0, det); // which means the determinant is 0
            }
            else { // otherwise encode the determinant in the matrix
                rref.set(0, 0, new ComplexNumber(0, 0));
            }
        }

        return rref;
    }

    /**
     * Computes the row-reduced echelon form of this Matrix by
     * Gaussian elimination with partial pivoting for numerical stability
     * @return the row-reduced echelon form of this matrix
     */
    public Matrix rref() {

        return rref(0); // just compute the regular rref
    }

    /**
     * Returns the rank of this matrix (the dimension of its image), which is the
     * number of pivot columns of the row-reduced echelon form
     * @return the rank of this matrix
     */
    public int rank() {

        int rk = 0; // count of pivot columns

        for (ComplexNumber[] row : this.rref().getData()) {
            boolean all_zero = true;
            for (ComplexNumber z : row) {
                if (!z.isZero()) { // we found a pivot
                    all_zero = false;
                    break;
                }
            }
            if (!all_zero) { // if we found a pivot, increment the count
                rk++;
            }
        }

        return rk; // count of pivots is rank
    }

    /**
     * Returns the nullity of this matrix (the dimension of its kernel)
     * @return the nullity of this matrix
     */
    public int nullity() {

        return cols() - rank(); // nullity + rank = no. columns
    }

    /**
     * Computes the LaTeX string representing the matrix
     * @return LaTeX code to generate the matrix
     */
    public String toLatexString() {

        String lstr = "\\begin{pmatrix}";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                lstr += getAt(i, j).toString();
                if (j != cols - 1) {
                    lstr += "&";
                }
            }
            if (i != rows - 1) {
                lstr += "\\\\";
            }
        }

        lstr += "\\end{pmatrix}";
        return lstr;
    }

    /**
     * Computes a string representation of this matrix
     * @return a string representation of this matrix
     */
    public String toString() {

        String mstr = "[";

        for (int i = 0; i < rows; i++) {
            if (i != 0) mstr += " "; // align left margin horizontally
            mstr += "[";

            for (int j = 0; j < cols; j++) {
                mstr += getAt(i, j).toString();
                if (j != cols - 1) mstr += ", "; // add commas until last element
                else {
                    mstr += "]";
                    if (i == rows - 1) mstr += "]"; // cap with final ]
                    else mstr += "\n"; // otherwise start again at next line
                }
            }
        }

        return mstr;
    }

    /**
     * Tells if two matrices are equal (if all of their corresponding elements are equal)
     * @param m the matrix to compare this one too
     * @return whether the two matrices are equal
     */
    public boolean equals(Matrix m) {

        // have to have matching dimension to be equal
        if (rows != m.rows() || cols != m.cols()) {
            return false;
        }

        // each element has to match for them to be equal
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!m.getAt(i, j).equals(matrix[i][j])) {
                    return false;
                }
            }
        }

        return true; // if it got this far, they are equal
    }
}
