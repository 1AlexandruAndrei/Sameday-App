package exception;

public class InvalidDataException extends Exception {
    public InvalidDataException(String cause) {
        super(cause);
    }
}