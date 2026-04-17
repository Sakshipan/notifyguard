package com.notifyguard.notify_service.Notify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role ;

    @Column(name ="age", nullable = false)
    private Integer age;

    @Email
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "Password")
    private String Password;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name= "quite_hours_start")
    private LocalTime quietHoursStart;

    @Column(name= "quite_hours_end")
    private LocalTime quietHoursEnd;

    @Column(name = "email_enabled", nullable = false)
    private boolean emailEnabled = true;

    @Column(name = "sms_enabled", nullable = false)
    private boolean smsEnabled = true;

    @Column(name = "push_enabled", nullable = false)
    private boolean pushEnabled = true;

    @Column(name = "webhook_enabled", nullable = false)
    private boolean webhookEnabled = false;

    @Column(name= "timezone")
    private String preferredTimezone = "Asia/Kolkata";

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
