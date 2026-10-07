package com.arigs.rms.exception;

/**
 * Raised when a requested operation violates RMS business rules.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
