package com.notifyguard.notify_service.Audit.Dtos;

//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyRuleRequest {
        private String name;
        private String description;
        private String eventType;
        private Integer thresholdCount;
        private Integer windowSeconds;




}
//name, description, eventType, thresholdCount, windowSeconds