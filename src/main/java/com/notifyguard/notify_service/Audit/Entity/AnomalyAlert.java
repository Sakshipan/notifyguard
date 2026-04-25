package com.notifyguard.notify_service.Audit.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anomaly_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false, updatable = false)
    private AnomalyRule rule;

    @Column(name = "actor_id", updatable = false)
    private String actorId;

    @Column(name = "triggered_count", updatable = false)
    private int triggeredCount;

    @Column(name = "ai_explanation", columnDefinition = "TEXT")
    private String aiExplanation;

    @Column(name = "severity")
    private String severity;

    @Column(name = "resolved", nullable = false)
    private boolean resolved = false;

    @Column(name = "triggered_at", updatable = false, nullable = false)
    private LocalDateTime triggeredAt;

    @PrePersist
    protected void onCreate() {
        triggeredAt = LocalDateTime.now();
    }
}



