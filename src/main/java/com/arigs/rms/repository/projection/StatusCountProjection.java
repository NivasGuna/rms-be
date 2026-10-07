package com.arigs.rms.repository.projection;

/**
 * Scalar projection for grouped status counts.
 */
public interface StatusCountProjection {

    Object getStatus();

    long getTotal();
}
