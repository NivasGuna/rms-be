package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Maps menus to permissions required for visibility or actions.
 */
@Getter
@Setter
@Entity
@Table(name = "menu_permissions", indexes = {
        @Index(name = "idx_menu_permissions_menu", columnList = "menu_id"),
        @Index(name = "idx_menu_permissions_permission", columnList = "permission_id")
})
public class MenuPermission extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
