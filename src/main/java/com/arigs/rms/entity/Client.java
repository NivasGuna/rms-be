package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Client organization master.
 */
@Getter
@Setter
@Entity
@Table(name = "clients", indexes = {
        @Index(name = "idx_clients_code", columnList = "code"),
        @Index(name = "idx_clients_business_unit", columnList = "business_unit_id"),
        @Index(name = "idx_clients_active", columnList = "is_active")
})
public class Client extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_unit_id")
    private BusinessUnit businessUnit;

    @Column(name = "gst_number", length = 30)
    private String gstNumber;

    @Column(length = 200)
    private String website;
}
