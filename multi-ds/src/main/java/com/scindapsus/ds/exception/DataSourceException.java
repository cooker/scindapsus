package com.scindapsus.ds.exception;

/**
 * @author wyh
 * @since 1.0
 */
public class DataSourceException extends RuntimeException {

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }
}
