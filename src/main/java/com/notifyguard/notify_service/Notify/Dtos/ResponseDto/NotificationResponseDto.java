package com.notifyguard.notify_service.Notify.Dtos.ResponseDto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private String id;
    private String campaignId;
    private String userId;
    private String channel;
    private String content;
    private String priority;
    private String status;
    private LocalDateTime createdAt;
}
