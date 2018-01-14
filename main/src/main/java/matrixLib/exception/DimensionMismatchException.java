package matrixLib.exception;

/**
 * Represents the exception that the dimensions of the matrices
 * do not correspond to the current operation
 * @author Bryan Cuccioli
 */

public class DimensionMismatchException extends RuntimeException {
	
	private static final long serialVersionUID = -3456325801725480063L;

	/**
	 * Construct a DimensionMismatchException with the default message
	 */
	public DimensionMismatchException() {
		super("The matrices do not have the correct dimensions.");
	}

	/**
	 * Construct a DimensionMismatchException with the given message
	 * @param message the message passed along by the exception
	 */
	public DimensionMismatchException(String message) {
		super(message);
	}

	/**
	 * Construct a DimensionMismatchException with the given cause
	 * @param cause the cause of the exception
	 */
	public DimensionMismatchException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a DimensionMismatchException with the given message and cause
	 * @param message the custom message
	 * @param cause the custom cause
	 */
	public DimensionMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Computes a string representation of this exception
	 */
	public String toString() {
		return "DimensionMismatchException - " + this.getMessage();
	}
}
