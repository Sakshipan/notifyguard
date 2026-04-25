package com.notifyguard.notify_service.Audit.controller;

import com.notifyguard.notify_service.Audit.Dtos.AnomalyAlertResponse;
import com.notifyguard.notify_service.Audit.Dtos.AnomalyRuleRequest;
import com.notifyguard.notify_service.Audit.Entity.AnomalyRule;
import com.notifyguard.notify_service.Audit.Service.AnomalyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@RequiredArgsConstructor
public class AnomalyController {

    private final AnomalyService anomalyService;

    @PostMapping("/rules")
    public ResponseEntity<AnomalyRule> createRule(@RequestBody AnomalyRuleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(anomalyService.createRule(request));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<AnomalyAlertResponse>> getAlerts() {
        return ResponseEntity.ok(anomalyService.getAlerts());
    }
}