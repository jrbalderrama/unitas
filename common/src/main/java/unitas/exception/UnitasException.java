package unitas.exception;

/**
 *
 * @author javier
 */
public class UnitasException extends Exception {

    public UnitasException() {
        super();
    }

    public UnitasException(String message) {
        super(message);
    }

    public UnitasException(Throwable throwable) {
        super(throwable);
    }

    public UnitasException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
