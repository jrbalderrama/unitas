package unitas.exception;

/**
 *
 * @author javier
 */

//TODO change to RuntimeException
public class UnitasRuntimeException extends Exception {

    public UnitasRuntimeException() {
        super();
    }

    public UnitasRuntimeException(String message) {
        super(message);
    }

    public UnitasRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public UnitasRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
