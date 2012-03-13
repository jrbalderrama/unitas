package unitas.exception;

/**
 *
 * @author javier
 */

public class RuntimeException extends Exception {

    public RuntimeException() {
        super();
    }

    public RuntimeException(String message) {
        super(message);
    }

    public RuntimeException(Throwable throwable) {
        super(throwable);
    }

    public RuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
