package com.arigs.rms.repository;

import com.arigs.rms.entity.OutboxEvent;
import com.arigs.rms.outbox.OutboxEventStatus;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OutboxEventRepository extends BaseRepository<OutboxEvent> {

    @Query("""
            select event from OutboxEvent event
            where event.status in :statuses
              and event.retryCount < :retryLimit
              and event.active = true
              and event.deleted = false
            order by event.createdDate asc
            """)
    List<OutboxEvent> findDispatchable(
            @Param("statuses") Collection<OutboxEventStatus> statuses,
            @Param("retryLimit") int retryLimit,
            Pageable pageable);

    long countByStatusAndActiveTrueAndDeletedFalse(OutboxEventStatus status);

    long countByStatusAndCreatedDateBeforeAndActiveTrueAndDeletedFalse(OutboxEventStatus status, Instant threshold);
}
