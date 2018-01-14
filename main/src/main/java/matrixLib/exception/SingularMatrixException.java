package matrixLib.exception;

/**
 * Represents the exception that the given matrix was singular,
 * when the current operation requires an invertible matrix
 * @author Bryan Cuccioli
 */

public class SingularMatrixException extends RuntimeException {
	
	private static final long serialVersionUID = -3930658714138010367L;

	/**
	 * Constructs a SingularMatrixException with the default message
	 */
	public SingularMatrixException() {
		super("The matrix is singular.");
	}

	/**
	 * Constructs a SingularMatrixException with a custom message
	 * @param message the custom message
	 */
	public SingularMatrixException(String message) {
		super(message);
	}

	/**
	 * Constructs a SingularMatrixException with a custom cause
	 * @param cause the custom cause
	 */
	public SingularMatrixException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a SingularMatrixException with a custom message and cause
	 * @param message the custom message
	 * @param cause the custom cause
	 */
	public SingularMatrixException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Computes a string representation of the exception
	 */
	public String toString() {
		return "SingularMatrixException - " + this.getMessage();
	}
}
