package com.notifyguard.notify_service.Dashboard.Dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCampaignBreakdownResponse {
    private String userId;
    private String userName;
    private String email;
    private long notificationsReceived;
    private String lastNotificationStatus;
}