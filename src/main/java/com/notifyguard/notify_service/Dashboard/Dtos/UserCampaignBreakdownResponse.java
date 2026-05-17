package com.notifyguard.notify_service.Dashboard.Dtos;

import lombok.*;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCampaignBreakdownResponse implements Serializable {
    private String userId;
    private String userName;
    private String email;
    private long notificationsReceived;
    private String lastNotificationStatus;
}