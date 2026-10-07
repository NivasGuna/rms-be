package com.arigs.rms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Configurable background job definition.
 */
@Getter
@Setter
@Entity
@Table(name = "background_job_definitions", indexes = {
        @Index(name = "idx_background_job_definitions_code", columnList = "code")
})
public class BackgroundJobDefinition extends BaseMasterEntity {

    @Column(name = "handler_name", nullable = false, length = 120)
    private String handlerName;

    @Column(name = "cron_expression", nullable = false, length = 120)
    private String cronExpression;
}
