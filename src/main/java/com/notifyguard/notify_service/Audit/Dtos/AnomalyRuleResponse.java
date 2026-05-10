package com.notifyguard.notify_service.Audit.Dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyRuleResponse {
    private String id;
    private String name;
    private String description;
    private String eventType;
    private Integer thresholdCount;
    private Integer windowSeconds;
    private boolean enabled;
}
