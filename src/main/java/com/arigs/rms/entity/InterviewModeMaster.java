package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Interview mode master.
 */
@Getter
@Setter
@Entity
@Table(name = "interview_modes", indexes = {
        @Index(name = "idx_interview_modes_code", columnList = "code")
})
public class InterviewModeMaster extends BaseMasterEntity {
}
