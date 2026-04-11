package com.notifyguard.notify_service.Notify.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaign_users")
public class CampaignUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Many users can belong to one campaign
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    // One user can be in many campaigns
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "best_channel")
    private ChannelType bestChannel; // null during discovery

    @Enumerated(EnumType.STRING)
    @Column(name = "individual_status")
    private CampaignUserStatus individualStatus;

    @Column(name = "messages_sent_count")
    private Integer messagesSentCount = 0;

    @Column(name = "messages_responded")
    private Integer messagesResponded = 0;

    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        enrolledAt = LocalDateTime.now();  // set enrolledAt here too
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
