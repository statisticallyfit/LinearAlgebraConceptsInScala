package matrixLib;

/**
 * Represents a real number
 * @author Bryan Cuccioli
 */
public class RealNumber extends ComplexNumber {

	/**
	 * Create a new RealNumber
	 * @param d the value of the real number
	 */
	public RealNumber(double d) {

		super(d, 0);
	}
	
	/**
	 * Returns the value of the real number
	 * @return the value of the real number
	 */
	public double getValue() {
		return this.Re();
	}
	
	/**
	 * Returns the string representation of the number
	 * @return the string representation of the number
	 */
	public String toString() {
		return Double.toString(this.Re());
	}
}
