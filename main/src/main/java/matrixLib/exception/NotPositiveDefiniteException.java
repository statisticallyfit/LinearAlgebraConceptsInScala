package matrixLib.exception;

/**
 * Represents the exception that the given matrix is not positive definite,
 * which is required by the Cholesky decomposition
 * @author Bryan Cuccioli
 */

public class NotPositiveDefiniteException extends RuntimeException {
	
	private static final long serialVersionUID = -4267603942007577557L;

	/**
	 * Constructs a NotPositiveDefiniteException with the default message
	 */
	public NotPositiveDefiniteException() {
		super("The matrix is not positive definite.");
	}

	/**
	 * Constructs a NotPositiveDefiniteException with a custom message
	 * @param message the custom message
	 */
	public NotPositiveDefiniteException(String message) {
		super(message);
	}

	/**
	 * Constructs a NotPositiveDefiniteException with a custom cause
	 * @param cause the custom cause
	 */
	public NotPositiveDefiniteException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a NotPositiveDefiniteException with a custom message and cause
	 * @param message the custom message
	 * @param cause the custom cause
	 */
	public NotPositiveDefiniteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Computes a string representation of this exception
	 */
	public String toString() {
		return "NotPositiveDefiniteException - " + this.getMessage();
	}
}
