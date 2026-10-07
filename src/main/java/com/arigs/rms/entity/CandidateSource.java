package com.arigs.rms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Candidate sourcing channel master.
 */
@Getter
@Setter
@Entity
@Table(name = "candidate_sources", indexes = {
        @Index(name = "idx_candidate_sources_code", columnList = "code")
})
public class CandidateSource extends BaseMasterEntity {
}
