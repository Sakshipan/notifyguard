package com.notifyguard.notify_service.Audit.Service;

import com.notifyguard.notify_service.Audit.Dtos.AnomalyAlertResponse;
import com.notifyguard.notify_service.Audit.Dtos.AnomalyRuleRequest;
import com.notifyguard.notify_service.Audit.Dtos.AnomalyRuleResponse;
import com.notifyguard.notify_service.Audit.Entity.AnomalyAlert;
import com.notifyguard.notify_service.Audit.Entity.AnomalyRule;
import com.notifyguard.notify_service.Audit.Repository.AnomalyAlertRepository;
import com.notifyguard.notify_service.Audit.Repository.AnomalyRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnomalyService {

    private final AnomalyRuleRepository anomalyRuleRepository;
    private final AnomalyAlertRepository anomalyAlertRepository;

    // creates a new anomaly rule and returns DTO
    public AnomalyRuleResponse createRule(AnomalyRuleRequest request) {
        AnomalyRule rule = AnomalyRule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .eventType(request.getEventType())
                .thresholdCount(request.getThresholdCount())
                .windowSeconds(request.getWindowSeconds())
                .enabled(true)
                .build();

        AnomalyRule saved = anomalyRuleRepository.save(rule);

        return AnomalyRuleResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .eventType(saved.getEventType())
                .thresholdCount(saved.getThresholdCount())
                .windowSeconds(saved.getWindowSeconds())
                .enabled(saved.isEnabled())
                .build();
    }

    // returns all unresolved alerts
    public List<AnomalyAlertResponse> getAlerts() {
        return anomalyAlertRepository.findByResolvedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // converts entity to DTO
    private AnomalyAlertResponse mapToResponse(AnomalyAlert alert) {
        return AnomalyAlertResponse.builder()
                .id(alert.getId())
                .actorId(alert.getActorId())
                .triggeredCount(alert.getTriggeredCount())
                .aiExplanation(alert.getAiExplanation())
                .severity(alert.getSeverity())
                .resolved(alert.isResolved())
                .triggeredAt(alert.getTriggeredAt())
                .build();
    }
}