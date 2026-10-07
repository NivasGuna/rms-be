package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Skill master for candidate and job matching.
 */
@Getter
@Setter
@Entity
@Table(name = "skills", indexes = {
        @Index(name = "idx_skills_code", columnList = "code"),
        @Index(name = "idx_skills_category", columnList = "category")
})
public class Skill extends BaseMasterEntity {

    @Column(length = 80)
    private String category;
}
