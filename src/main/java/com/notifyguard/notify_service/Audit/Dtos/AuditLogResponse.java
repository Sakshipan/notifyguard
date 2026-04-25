package com.notifyguard.notify_service.Audit.Dtos;
import lombok.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {
       private String id;
        private String eventType;
        private String actorId;
        private String actorType;
        private String resourceType;
        private String resourceId;
        private String beforeState;
        private String afterState;
        private LocalDateTime createdAt;

}
//id, eventType, actorId, actorType, resourceType, resourceId, beforeState, afterState, createdAt