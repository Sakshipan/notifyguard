package com.notifyguard.notify_service.Audit.Service;


import com.notifyguard.notify_service.Audit.Dtos.AuditLogResponse;
import com.notifyguard.notify_service.Audit.Entity.AuditLog;
import com.notifyguard.notify_service.Audit.Repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class AuditService {

        private final AuditLogRepository auditLogRepository;


        // saves an audit log entry — called internally whenever something happens
        public void log(AuditLog auditLog) {
            try {
                auditLogRepository.save(auditLog);
            } catch (Exception e) {
                log.error("Failed to save audit log: {}", e.getMessage());
            }
        }

        // fetches logs with filters
        public List<AuditLogResponse> getLogs(String actorId, String eventType, LocalDateTime from, LocalDateTime to) {
            List<AuditLog> logs;

            if (actorId != null) {
                logs = auditLogRepository.findByActorId(actorId);
            } else if (eventType != null) {
                logs = auditLogRepository.findByEventType(
                        AuditLog.EventType.valueOf(eventType));
            } else if (from != null && to != null) {
                logs = auditLogRepository.findByCreatedAtBetween(from, to);
            } else {
                logs = auditLogRepository.findAll();
            }

            return logs.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        // converts entity to DTO
        private AuditLogResponse mapToResponse(AuditLog log) {
            return AuditLogResponse.builder()
                    .id(log.getId())
                    .eventType(log.getEventType().toString())
                    .actorId(log.getActorId())
                    .actorType(log.getActorType().toString())
                    .resourceType(log.getResourceType())
                    .resourceId(log.getResourceId())
                    .beforeState(log.getBeforeState())
                    .afterState(log.getAfterState())
                    .createdAt(log.getCreatedAt())
                    .build();
        }
    }

