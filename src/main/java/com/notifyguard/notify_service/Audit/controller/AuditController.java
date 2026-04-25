package com.notifyguard.notify_service.Audit.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.notifyguard.notify_service.Audit.Dtos.AuditLogResponse;
import com.notifyguard.notify_service.Audit.Service.AuditService;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {


    private final AuditService auditService;
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
        }

