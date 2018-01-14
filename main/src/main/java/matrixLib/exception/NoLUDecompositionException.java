package matrixLib.exception;

/**
 * Represents the exception that the given matrix does not admit an LU decomposition
 * @author Bryan Cuccioli
 */

public class NoLUDecompositionException extends RuntimeException {
	
	private static final long serialVersionUID = 218089983681707090L;

	/**
	 * Constructs a NoLUDecompositionException with the default message
	 */
	public NoLUDecompositionException() {
		super("The matrix does not admit an LU decomposition.");
	}

	/**
	 * Constructs a NoLUDecompositionException with a custom message
	 * @param message the custom message
	 */
	public NoLUDecompositionException(String message) {
		super(message);
	}

	/**
	 * Constructs a NoLUDecompositionException with a custom cause
	 * @param cause the custom cause
	 */
	public NoLUDecompositionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a NoLUDecompositionException with a custom message and cause
	 * @param message the custom message
	 * @param cause the custom cause
	 */
	public NoLUDecompositionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Computes a string representation of this exception
	 */
	public String toString() {
		return "NoLUDecompositionException - " + this.getMessage();
	}
}
