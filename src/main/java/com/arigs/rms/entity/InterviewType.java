package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Interview type master.
 */
@Getter
@Setter
@Entity
@Table(name = "interview_types", indexes = {
        @Index(name = "idx_interview_types_code", columnList = "code")
})
public class InterviewType extends BaseMasterEntity {
}
