package matrixLib;

/**
 * Represents a complex number, which has a real and imaginary part.
 * This enables using matrices over the field C instead of just R.
 * @author Bryan Cuccioli
 */
public class ComplexNumber implements Comparable<ComplexNumber> {

	private double re;
	private double im;
	private static double epsilon = 1e-13; // default epsilon

	/**
	 * Creates the complex number z = (0, 0)
	 */
	public ComplexNumber() {
		this.re = 0;
		this.im = 0;
	}
	
	/**
	 * Creates a complex number with given real and imaginary components
	 * @param re The real part of z
	 * @param im The imaginary part of z
	 */
	public ComplexNumber(double re, double im) {
		
		this.re = re;
		this.im = im;
	}

	/**
	 * Returns the real part of the complex number
	 * @return The real part of the complex number
	 */
	public double Re() {
		return this.re;
	}
	
	/**
	 * Returns the imaginary part of the complex number
	 * @return The imaginary part of the complex number
	 */
	public double Im() {
		return this.im;
	}
	
	/**
	 * Sets the epsilon to use in comparing complex numbers
	 * @param eps the epsilon to use
	 */
	public static void setEpsilon(double eps) {
		epsilon = eps;
	}
	
	/**
	 * Gets the epsilon currently in use in comparing complex numbers
	 * @return the epsilon currently in use
	 */
	public static double getEpsilon() {
		return epsilon;
	}
	
	/**
	 * Returns the complex number created by adding this to z
	 * @param z The complex number to add to this
	 * @return The complex number created by adding this with z
	 */
	public ComplexNumber add(ComplexNumber z) {
		
		return new ComplexNumber(z.Re() + re, z.Im() + im);
	}
	
	/**
	 * Returns the complex number created by subtracting the given complex number from this one
	 * @param z The complex number to subtract from this
	 * @return The complex number created by subtracting z from this
	 */
	public ComplexNumber subtract(ComplexNumber z) {
		
		return new ComplexNumber(re - z.Re(), im - z.Im());
	}
	
	/**
	 * Returns the complex number created by multiplying this one with the given,
	 * using the standard definition of multiplication for complex numbers
	 * @param z The complex number to multiply with this
	 * @return The complex number created by multiplying this with z
	 */
	public ComplexNumber multiply(ComplexNumber z) {
		
		return new ComplexNumber(re*z.Re() - im*z.Im(), re*z.Im() + im*z.Re());
	}
	
	/**
	 * Divides this complex number by z (through multiplying by the inverse)
	 * @param z the complex number by which to divide this one
	 * @return the quotient of this complex number and the argument
	 * @throws ArithmeticException trying to divide by 0
	 */
	public ComplexNumber divide(ComplexNumber z) throws ArithmeticException {
		
		if (z.isZero()) { // can't allow division by 0
			 throw new ArithmeticException();
		}
		
		// this is more numerically stable than the concise formulation
		ComplexNumber quot = this.multiply(z.conjugate());
		double recmod = (z.Re()*z.Re() + z.Im()*z.Im());
		return new ComplexNumber(quot.Re()/recmod, quot.Im()/recmod);
	}

	/**
	 * Returns the reciprocal of this complex number
	 * @return the reciprocal of this complex number
	 * @throws ArithmeticException trying to take the reciprocal of zero
	 */
	public ComplexNumber reciprocal() throws ArithmeticException {
		
		if (this.isZero()) { // can't compute the reciprocal of 0
			 throw new ArithmeticException();
		}
		
		// this is more numerically stable than the concise formulation
		ComplexNumber quot = this.conjugate();
		double recmod = (re*re + im*im);
		return new ComplexNumber(quot.Re()/recmod, quot.Im()/recmod);
	}
	
	/**
	 * Scales a complex number by a factor
	 * @param f the scaling factor
	 * @return the complex number scaled by f
	 */
	public ComplexNumber multiply(double f) {
		
		return new ComplexNumber(re*f, im*f);
	}
	
	/**
	 * Returns the complex conjugate a-bi of this complex number
	 * @return The complex conjugate of this complex number
	 */
	public ComplexNumber conjugate() {
		
		return new ComplexNumber(re, -im);
	}

	/**
	 * Returns the negation (additive inverse) of this complex number
	 * @return the negative of this complex number
	 */
	public ComplexNumber negative() {
		return new ComplexNumber(-re, -im);
	}
	
	/**
	 * Returns the modulus |z| of this complex number, sqrt(Re^2+Im^2)
	 * @return The absolute value of this complex number
	 */
	public double abs() {
		
		// try simple formulas to preserve precision
		if (re == 0) {
			return Math.abs(im);
		}
		else if (im == 0) {
			return Math.abs(re);
		}
		// otherwise resort to the more complicated modulus formula:
		else {
			return Math.sqrt(re*re + im*im);
		}
	}

	/**
	 * Returns the argument of this complex number, which is equivalent to computing atan2
	 * @return the argument of this complex number
	 * @throws ArtithmeticException trying to take the argument of 0
	 */
	public double arg() throws ArithmeticException {
		
		// arg is undefined for the complex number (0,0)
		if (re == 0.0 && im == 0.0) {
			 throw new ArithmeticException();
		}
		
		if (re == 0.0) { // imaginary numbers are vertical in the plane
			// sign determines whether the vector points up or down
			return Math.PI/2 * Math.signum(im);
		}
		else if (re > 0) { // can use regular atan on the right side of the plane
			return Math.atan(im/re);
		}
		else {
			// need to add or subtract pi if in quadrant 3 or 4
			return Math.atan(im/re) + Math.PI * Math.signum(im);
		}
	}

	/**
	 * Computes the principle square root of this complex number
	 * (the one which has positive real component)
	 * @return the principle square root of this complex number
	 */
	public ComplexNumber sqrt() {
		
		double a = this.Re(), b = this.Im();
		
		if (b == 0) { // see if we can just compute simple sqrt
			if (Math.abs(a) < epsilon) return new ComplexNumber(0,0);
			if (a >= 0) {
				return new ComplexNumber(Math.sqrt(a),0);
			}
			else { // compute imaginary square root, sqrt(a)=isqrt(-a)
				return new ComplexNumber(0, Math.sqrt(-a));
			}
		}
		
		// use the square root formula for a complex number
		double inner = Math.sqrt(a*a+b*b);
		double p = 1.0/Math.sqrt(2) * Math.sqrt(inner+a);
		double q = 1.0/Math.sqrt(2) * Math.sqrt(inner-a) * Math.signum(b);
		
		return new ComplexNumber(p, q);
	}
	
	/**
	 * Returns the a + bi form of this complex number
	 * @return The string representation of this number
	 */
	public String toString() {

		// return only a or bi if the number is real or imaginary
		if (re == 0) {
			if (im == 0) {
				return "0";
			}
			return Double.toString(im) + "i";
		}
		else if (im == 0) {
			return Double.toString(re);
		}
		// otherwise have to return a + bi
		else {
			return re + " + " + im + "i";
		}
	}
	
	/**
	 * Tells whether two complex numbers are equal
	 * @param z The complex number to compare with this one
	 * @return a boolean indicating whether the two complex numbers are equal
	 */
	public boolean equals(ComplexNumber z) {
		
		return Math.abs(re-z.Re()) <= epsilon
			&& Math.abs(im-z.Im()) <= epsilon;
	}
	
	/**
	 * Tells whether this complex number is zero
	 * @return a boolean indicating whether this is the complex number equal to 0
	 */
	public boolean isZero() {
		
		return Math.abs(this.re)<=epsilon && Math.abs(this.im)<=epsilon;
	}

	/**
	 * Compares this to z by comparing by absolute value, then by argument.
	 * This is used for sorting lists of complex numbers, when it depends only that the comparisons are consistent.
	 * @param z the complex number to compare with this
	 * @return 1, -1, or 0 if the given number is greater, less, or equal, according to the above description
	 */
	public int compareTo(ComplexNumber z) {
		
		if (this.abs() > z.abs()) return -1;
		else if (this.abs() < z.abs()) return 1;
		else {
			if (this.arg() > z.arg()) return -1;
			else if (this.arg() > z.arg()) return 1;
			else return 0;
		}
	}
}
