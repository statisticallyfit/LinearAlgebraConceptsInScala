package matrixLib.exception;

/**
 * Represents the exception that the given matrix is not Hermitian,
 * which is required for the current operation
 * @author Bryan Cuccioli
 */

public class NotHermitianException extends RuntimeException {
	
	private static final long serialVersionUID = 2960821471544223636L;

	/**
	 * Constructs a NotHermitianException with the default message
	 */
	public NotHermitianException() {
		super("The matrix is not Hermitian.");
	}

	/**
	 * Constructs a NotHermitianException with a custom message
	 * @param message the custom message
	 */
	public NotHermitianException(String message) {
		super(message);
	}

	/**
	 * Constructs a NotHermitianException with a custom cause
	 * @param cause the custom cause
	 */
	public NotHermitianException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a NotHermitianException with a custom message and cause
	 * @param message the custom message
	 * @param cause the custom cause
	 */
	public NotHermitianException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Computes a string representation of this exception
	 */
	public String toString() {
		return "NotHermitianException - " + this.getMessage();
	}
}
