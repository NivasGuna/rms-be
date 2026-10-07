package com.arigs.rms.common;

/**
 * Field-level or business-level API error.
 */
public record ApiError(String field, String message) {
}
