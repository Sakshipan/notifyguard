
package com.notifyguard.notify_service.Audit.Service;

import com.notifyguard.notify_service.Audit.Dtos.AnomalyAlertResponse;
import com.notifyguard.notify_service.Audit.Dtos.AnomalyRuleRequest;
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


    // creates a new anomaly rule
    public AnomalyRule createRule(AnomalyRuleRequest request) {
        AnomalyRule rule = AnomalyRule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .eventType(request.getEventType())
                .thresholdCount(request.getThresholdCount())
                .windowSeconds(request.getWindowSeconds())
                .enabled(true)
                .build();

        return anomalyRuleRepository.save(rule);
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
