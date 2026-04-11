package com.notifyguard.notify_service.Notify.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "message_content", columnDefinition = "TEXT")
    private String messageContent;

    @Column(name = "discovery_days")
    private Integer discoveryDays;

    @Column(name = "optimized_days")
    private Integer optimizedDays;

    @Column(name = "cycle_count")
    private Integer cycleCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_phase")
    private CampaignPhase currentPhase;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CampaignStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "phase_started_at")
    private LocalDateTime phaseStartedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
