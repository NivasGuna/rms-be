package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Location master used by jobs and candidates.
 */
@Getter
@Setter
@Entity
@Table(name = "locations", indexes = {
        @Index(name = "idx_locations_code", columnList = "code"),
        @Index(name = "idx_locations_city", columnList = "city")
})
public class RmsLocation extends BaseMasterEntity {

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    private String country;
}
