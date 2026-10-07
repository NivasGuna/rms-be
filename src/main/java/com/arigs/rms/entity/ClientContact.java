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
 * Client contact person master.
 */
@Getter
@Setter
@Entity
@Table(name = "client_contacts", indexes = {
        @Index(name = "idx_client_contacts_client", columnList = "client_id"),
        @Index(name = "idx_client_contacts_email", columnList = "email")
})
public class ClientContact extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "contact_name", nullable = false, length = 150)
    private String contactName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 120)
    private String designation;

    @Column(name = "primary_contact", nullable = false)
    private boolean primaryContact;
}
