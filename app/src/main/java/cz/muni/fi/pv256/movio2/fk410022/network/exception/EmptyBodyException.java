package cz.muni.fi.pv256.movio2.fk410022.network.exception;

public class EmptyBodyException extends Exception {

    public EmptyBodyException() {
        super();
    }

    public EmptyBodyException(String message) {
        super(message);
    }

    public EmptyBodyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyBodyException(Throwable cause) {
        super(cause);
    }
}
