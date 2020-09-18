package io.github.onlyeat3.fastmapper;

public class FastMapperException extends RuntimeException {
    public FastMapperException() {
        super();
    }

    public FastMapperException(String message) {
        super(message);
    }

    public FastMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastMapperException(Throwable cause) {
        super(cause);
    }

    protected FastMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
