package com.notifyguard.notify_service.Notify.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Many notifications belong to one campaign
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable=false)
    private Campaign campaign;

    // Recipient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private ChannelType channel;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private NotificationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus status;

    @Column(name = "ai_reason")
    private String aiReason;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
