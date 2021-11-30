package com.bewg.pd.common.exception;

/**
 * @author lizy
 */
public class PdException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PdException(String message) {
        super(message);

    }

    public PdException(Throwable cause) {
        super(cause);
    }

    public PdException(String message, Throwable cause) {
        super(message, cause);
    }
}
