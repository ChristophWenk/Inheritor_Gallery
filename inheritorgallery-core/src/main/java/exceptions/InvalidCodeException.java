package exceptions;

public class InvalidCodeException extends Exception {
    public InvalidCodeException(String errorMessage) {
        super(errorMessage);
    }
}
