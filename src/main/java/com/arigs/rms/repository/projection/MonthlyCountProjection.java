package com.arigs.rms.repository.projection;

/**
 * Scalar projection for monthly trend counts.
 */
public interface MonthlyCountProjection {

    int getYear();

    int getMonth();

    long getTotal();
}
