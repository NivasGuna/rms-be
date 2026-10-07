package com.arigs.rms.exception;

/**
 * Raised when an active entity cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
