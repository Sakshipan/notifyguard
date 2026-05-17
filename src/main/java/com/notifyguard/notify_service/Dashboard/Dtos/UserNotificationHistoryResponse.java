package com.notifyguard.notify_service.Dashboard.Dtos;

import lombok.*;
import java.time.LocalDateTime;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationHistoryResponse implements Serializable {
    private String notificationId;
    private String campaignName;
    private String channel;
    private String status;
    private String content;
    private LocalDateTime sentAt;
}