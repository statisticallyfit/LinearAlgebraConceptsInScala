package matrixLib.exception;

/**
 * Represents the exception that the given matrix is not square,
 * which is required for the current operation
 * @author Bryan Cuccioli
 */

public class NotSquareException extends RuntimeException {
	
	private static final long serialVersionUID = 218089983681707090L;

	/**
	 * Constructs a NotSquareException with the default message
	 */
	public NotSquareException() {
		super("The matrix is not square.");
	}

	/**
	 * Constructs a NotSquareException with a custom message
	 * @param message the custom message
	 */
	public NotSquareException(String message) {
		super(message);
	}

	/**
	 * Constructs a NotSquareException with a custom cause
	 * @param cause the custom cause
	 */
	public NotSquareException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a NotSquareException with a custom message and cause
	 * @param message the custom message
	 * @param cause the custom cause
	 */
	public NotSquareException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Computes a string representation of this exception
	 */
	public String toString() {
		return "NotSquareException - " + this.getMessage();
	}
}
