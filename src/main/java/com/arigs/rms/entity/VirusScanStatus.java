package com.arigs.rms.entity;

/**
 * Virus scanning lifecycle for uploaded files.
 */
public enum VirusScanStatus {
    PENDING,
    CLEAN,
    INFECTED,
    FAILED,
    SKIPPED
}
