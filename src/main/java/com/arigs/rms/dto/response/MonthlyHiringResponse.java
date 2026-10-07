package com.arigs.rms.dto.response;

/**
 * Monthly joining count.
 */
public record MonthlyHiringResponse(int year, int month, long total) {
}
