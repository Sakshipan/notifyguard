package com.notifyguard.notify_service.Notify.service;

import com.notifyguard.notify_service.Audit.Entity.AuditLog;
import com.notifyguard.notify_service.Audit.Service.AuditService;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.NotificationRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.NotificationResponseDto;
import com.notifyguard.notify_service.Notify.entity.*;
import com.notifyguard.notify_service.Notify.repository.CampaignRepository;
import com.notifyguard.notify_service.Notify.repository.CampaignUserRepository;
import com.notifyguard.notify_service.Notify.repository.NotificationRepository;
import com.notifyguard.notify_service.Notify.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {
    private final CampaignRepository campaignRepository;
    private final CampaignUserRepository campaignUserRepository;
    private final AuditService auditService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public NotificationResponseDto sendNotification(NotificationRequestDto request){
        Campaign campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new RuntimeException(
                        "Campaign not found: " + request.getCampaignId()));


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found: " + request.getUserId()));

        campaignUserRepository.findByCampaignIdAndUserId(
                        request.getCampaignId(), request.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User is not enrolled in this campaign"));
        ChannelType channel = request.getChannel();
        checkChannelPreference(user, channel);
        checkQuietHours(user);
        String content = request.getContent() != null
                ? request.getContent()
                : campaign.getMessageContent();
        Notification notification = new Notification();
        notification.setCampaign(campaign);
        notification.setUser(user);
        notification.setChannel(channel);
        notification.setContent(content);
        notification.setPriority(NotificationPriority.NORMAL);
        notification.setStatus(NotificationStatus.QUEUED);
        Notification saved = notificationRepository.save(notification);
        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.NOTIFICATION_SENT)
                .actorId(getCurrentUserId())
                .actorType(AuditLog.ActorType.USER)
                .resourceType("NOTIFICATION")
                .resourceId(saved.getId())
                .beforeState(null)
                .afterState("status=QUEUED, channel=" + channel
                        + ", userId=" + user.getId())
                .build();
        auditService.log(auditLog);

        return mapToResponse(saved);
    }
    private void checkChannelPreference(User user, ChannelType channel) {
        boolean enabled = switch (channel) {
            case EMAIL             -> user.isEmailEnabled();
            case SMS               -> user.isSmsEnabled();
            case PUSH_NOTIFICATION -> user.isPushEnabled();
            case WHATSAPP          -> user.isWhatsappEnabled();
        };
        if (!enabled) {
            throw new RuntimeException(
                    "User has opted out of " + channel + " notifications");
        }
    }
    private void checkQuietHours(User user) {
        if (user.getQuietHoursStart() == null
                || user.getQuietHoursEnd() == null) {
            return; // no quiet hours set, proceed
        }

        LocalTime now = LocalTime.now(
                ZoneId.of(user.getPreferredTimezone() != null
                        ? user.getPreferredTimezone()
                        : "Asia/Kolkata"));

        LocalTime start = user.getQuietHoursStart();
        LocalTime end = user.getQuietHoursEnd();

        boolean inQuietHours;
        if (start.isAfter(end)) {
            inQuietHours = now.isAfter(start) || now.isBefore(end);
        } else {
            inQuietHours = now.isAfter(start) && now.isBefore(end);
        }

        if (inQuietHours) {
            throw new RuntimeException(
                    "User is in quiet hours. Try after "
                            + end);
        }
    }
    private String getCurrentUserId() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
    private NotificationResponseDto mapToResponse(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .campaignId(notification.getCampaign().getId())
                .userId(notification.getUser().getId())
                .channel(notification.getChannel().toString())
                .content(notification.getContent())
                .priority(notification.getPriority().toString())
                .status(notification.getStatus().toString())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public NotificationResponseDto getNotificationById(String id){
        Notification notification= notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        return mapToResponse(notification);

    }
    @Transactional
    public List<NotificationResponseDto> getNotificationsByUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found: " + userId));

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Transactional
    public List<NotificationResponseDto> getNotificationsByCampaign(
            String campaignId) {
        campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException(
                        "Campaign not found: " + campaignId));

        return notificationRepository.findByCampaignId(campaignId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    public void updateNotificationStatus(String notificationId,
                                         NotificationStatus newStatus) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new RuntimeException(
                        "Notification not found: " + notificationId));

        NotificationStatus previousStatus = notification.getStatus();

        notification.setStatus(newStatus);

        // set sentAt when status moves to SENT
        if (newStatus == NotificationStatus.SENT) {
            notification.setSentAt(LocalDateTime.now());
        }

        notificationRepository.save(notification);

        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.NOTIFICATION_SENT)
                .actorId("SYSTEM")
                .actorType(AuditLog.ActorType.SYSTEM)
                .resourceType("NOTIFICATION")
                .resourceId(notificationId)
                .beforeState("status=" + previousStatus)
                .afterState("status=" + newStatus)
                .build();
        auditService.log(auditLog);
    }
    //Thing 1 — updateNotificationStatus is called by SYSTEM
    //This method is not called by a human through a controller. It will be called internally by RabbitMQ workers in Phase 10. That is why actorId is "SYSTEM" and actorType is SYSTEM.

    @Transactional
    public void trackResponse(String notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new RuntimeException(
                        "Notification not found: " + notificationId));

        String userId = notification.getUser().getId();
        String campaignId = notification.getCampaign().getId();

        // update messagesResponded on CampaignUser
        CampaignUser campaignUser = campaignUserRepository
                .findByCampaignIdAndUserId(campaignId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "CampaignUser not found"));

        campaignUser.setMessagesResponded(
                campaignUser.getMessagesResponded() + 1);
        campaignUserRepository.save(campaignUser);

        // TODO: update Redis behavior data in Phase 11

        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.NOTIFICATION_RESPONDED)
                .actorId(userId)
                .actorType(AuditLog.ActorType.USER)
                .resourceType("NOTIFICATION")
                .resourceId(notificationId)
                .beforeState(null)
                .afterState("responded=true, channel="
                        + notification.getChannel())
                .build();
        auditService.log(auditLog);
    }

}
