package com.notifyguard.notify_service.Audit.controller;

import com.notifyguard.notify_service.Audit.Dtos.AuditLogResponse;
import com.notifyguard.notify_service.Audit.Service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    // GET /api/audit/logs
    @GetMapping("/logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<AuditLogResponse>> getLogs(
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to) {
        return ResponseEntity.ok(
                auditService.getLogs(actorId, eventType, from, to));
    }

    // GET /api/audit/logs/actor/{actorId}
    @GetMapping("/logs/actor/{actorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<AuditLogResponse>> getLogsByActor(
            @PathVariable String actorId) {
        return ResponseEntity.ok(auditService.getLogs(actorId, null, null, null));
    }

    // GET /api/audit/logs/event/{type}
    @GetMapping("/logs/event/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<AuditLogResponse>> getLogsByEventType(
            @PathVariable String type) {
        return ResponseEntity.ok(auditService.getLogs(null, type, null, null));
    }

    // GET /api/audit/logs/resource/{id}
    @GetMapping("/logs/resource/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<AuditLogResponse>> getLogsByResource(
            @PathVariable String id) {
        return ResponseEntity.ok(auditService.getLogsByResourceId(id));
    }

    // GET /api/audit/logs/range
    @GetMapping("/logs/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<AuditLogResponse>> getLogsByRange(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(auditService.getLogs(null, null, from, to));
    }
}