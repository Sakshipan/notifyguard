package com.notifyguard.notify_service.Audit.Dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AnomalyAlertResponse {

        private String id;
        private String actorId;
        private Integer triggeredCount;
        private String aiExplanation;
        private String severity;
        private boolean resolved;
        private LocalDateTime triggeredAt;


}
//id → String
//actorId → String
//triggeredCount → Integer
//aiExplanation → String
//severity → String
//resolved → boolean
//triggeredAt → LocalDateTime

