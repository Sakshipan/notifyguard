package com.notifyguard.notify_service.Audit.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Immutable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "event_id", nullable = false, unique = true, updatable = false)
    private String eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, updatable = false)
    private EventType eventType;

    @Column(name = "actor_id", nullable = false, updatable = false)
    private String actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", nullable = false, updatable = false)
    private ActorType actorType;

    @Column(name = "resource_type", nullable = false, updatable = false)
    private String resourceType;

    @Column(name = "resource_id", updatable = false)
    private String resourceId;

    @Column(name = "before_state", columnDefinition = "TEXT", updatable = false)
    private String beforeState;

    @Column(name = "after_state", columnDefinition = "TEXT", updatable = false)
    private String afterState;

    @Column(name = "ip_address", updatable = false)
    private String ipAddress;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum EventType {
        NOTIFICATION_SENT,
        NOTIFICATION_FAILED,
        NOTIFICATION_RETRIED,
        NOTIFICATION_RESPONDED,
        CAMPAIGN_CREATED,
        CAMPAIGN_PHASE_SWITCHED,
        CAMPAIGN_COMPLETED,
        USER_OPT_OUT,
        USER_OPT_IN,
        TEMPLATE_CREATED,
        TEMPLATE_UPDATED,
        ANOMALY_DETECTED
    }

    public enum ActorType {
        USER,
        SERVICE,
        SYSTEM
    }
}