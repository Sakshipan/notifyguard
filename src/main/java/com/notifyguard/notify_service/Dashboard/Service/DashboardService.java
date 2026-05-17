package com.notifyguard.notify_service.Dashboard.Service;

import com.notifyguard.notify_service.Dashboard.Dtos.*;
import com.notifyguard.notify_service.Notify.entity.Notification;
import com.notifyguard.notify_service.Notify.repository.CampaignRepository;
import com.notifyguard.notify_service.Notify.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final CampaignRepository campaignRepository;
    private final NotificationRepository notificationRepository;

    // GET /api/dashboard/campaign/{id}/summary
    @Cacheable(value = "campaignSummary", key = "#campaignId")
    public CampaignSummaryResponse getCampaignSummary(String campaignId) {
        log.info("Fetching campaign summary from DB for id: {}", campaignId);
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        List<Notification> notifications = notificationRepository.findByCampaignId(campaignId);

        long totalUsers = notifications.stream()
                .map(n -> n.getUser().getId())
                .distinct()
                .count();

        return CampaignSummaryResponse.builder()
                .campaignId(campaign.getId())
                .companyName(campaign.getCompanyName())
                .status(campaign.getStatus() != null ? campaign.getStatus().name() : null)
                .currentPhase(campaign.getCurrentPhase() != null ? campaign.getCurrentPhase().name() : null)
                .cycleCount(campaign.getCycleCount())
                .totalNotificationsSent(notifications.size())
                .totalUsersTargeted(totalUsers)
                .build();
    }

    // GET /api/dashboard/campaign/{id}/users
    @Cacheable(value = "campaignUsers", key = "#campaignId")
    public List<UserCampaignBreakdownResponse> getUsersInCampaignBreakdown(String campaignId) {
        log.info("Fetching campaign users from DB for id: {}", campaignId);
        List<Notification> notifications = notificationRepository.findByCampaignId(campaignId);

        return notifications.stream()
                .collect(Collectors.groupingBy(n -> n.getUser().getId()))
                .entrySet().stream()
                .map(entry -> {
                    var user = entry.getValue().get(0).getUser();
                    var last = entry.getValue().get(entry.getValue().size() - 1);
                    return UserCampaignBreakdownResponse.builder()
                            .userId(user.getId())
                            .userName(user.getName())
                            .email(user.getEmail())
                            .notificationsReceived(entry.getValue().size())
                            .lastNotificationStatus(last.getStatus() != null ? last.getStatus().name() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // GET /api/dashboard/channel-performance
    @Cacheable(value = "channelPerformance")
    public List<ChannelPerformanceResponse> getChannelPerformance() {
        log.info("Fetching channel performance from DB");
        List<Notification> all = notificationRepository.findAll();

        return all.stream()
                .collect(Collectors.groupingBy(n -> n.getChannel().name()))
                .entrySet().stream()
                .map(entry -> {
                    var list = entry.getValue();
                    long sent = list.size();
                    long delivered = list.stream()
                            .filter(n -> n.getStatus() != null &&
                                    n.getStatus().name().equals("DELIVERED"))
                            .count();
                    long failed = list.stream()
                            .filter(n -> n.getStatus() != null &&
                                    n.getStatus().name().equals("FAILED"))
                            .count();
                    return ChannelPerformanceResponse.builder()
                            .channel(entry.getKey())
                            .totalSent(sent)
                            .totalDelivered(delivered)
                            .totalFailed(failed)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // GET /api/dashboard/users/{userId}/history
    @Cacheable(value = "userHistory", key = "#userId")
    public List<UserNotificationHistoryResponse> getUserNotificationHistory(String userId) {
        log.info("Fetching user notification history from DB for userId: {}", userId);
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(n -> UserNotificationHistoryResponse.builder()
                        .notificationId(n.getId())
                        .campaignName(n.getCampaign().getCompanyName())
                        .channel(n.getChannel() != null ? n.getChannel().name() : null)
                        .status(n.getStatus() != null ? n.getStatus().name() : null)
                        .content(n.getContent())
                        .sentAt(n.getSentAt())
                        .build())
                .collect(Collectors.toList());
    }

    // GET /api/dashboard/unresponsive-users
    @Cacheable(value = "unresponsiveUsers")
    public List<UserCampaignBreakdownResponse> getUnresponsiveUsers() {
        log.info("Fetching unresponsive users from DB");
        return notificationRepository.findAll()
                .stream()
                .filter(n -> n.getStatus() != null &&
                        n.getStatus().name().equals("FAILED"))
                .collect(Collectors.groupingBy(n -> n.getUser().getId()))
                .entrySet().stream()
                .map(entry -> {
                    var user = entry.getValue().get(0).getUser();
                    return UserCampaignBreakdownResponse.builder()
                            .userId(user.getId())
                            .userName(user.getName())
                            .email(user.getEmail())
                            .notificationsReceived(entry.getValue().size())
                            .lastNotificationStatus("FAILED")
                            .build();
                })
                .collect(Collectors.toList());
    }
}