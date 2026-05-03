package com.notifyguard.notify_service.Notify.Dtos.RequestDto;

import com.notifyguard.notify_service.Notify.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDto {
    private String campaignId;
    private String userId;
    private ChannelType channel;
    private String content;
}
