package com.notifyguard.notify_service.Audit.Repository;

import com.notifyguard.notify_service.Audit.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

    @Repository
    public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
        //findByActorId
        //findByEventType
        //findByResourceId
        //findByCreatedAtBetween
        //countByActorIdAndCreatedAtAfter

       /* List<AuditLog> findByActorId(String actorId);

        List<AuditLog> findByEventType(AuditLog.EventType eventType);

        List<AuditLog> findByResourceId(String resourceId);

        List<AuditLog>findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
        long countByActorIdAndCreatedAtAfter( String actorId, LocalDateTime after);*/




    }
