package exceptions;

/**
 * Exception class for invalid source code given by a user.
 */
public class InvalidCodeException extends Exception {
    public InvalidCodeException(String errorMessage) {
        super(errorMessage);
    }
}
