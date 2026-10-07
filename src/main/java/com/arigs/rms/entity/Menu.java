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
 * UI menu registry for permission assignment.
 */
@Getter
@Setter
@Entity
@Table(name = "menus", indexes = {
        @Index(name = "idx_menus_code", columnList = "code"),
        @Index(name = "idx_menus_parent", columnList = "parent_menu_id")
})
public class Menu extends BaseMasterEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_id")
    private Menu parentMenu;

    @Column(length = 200)
    private String route;

    @Column(length = 80)
    private String icon;
}
