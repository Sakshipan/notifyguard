package com.notifyguard.notify_service.Notify.Redis;

import com.notifyguard.notify_service.Notify.entity.ChannelType;
import lombok.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChannelBehavior {
    private String userId;

    private ChannelType channel;

    private int totalSent;

    private int totalResponded;

    private double responseRate;

    private LocalDateTime lastRespondedAt;

    private int morningResponses;

    private int afternoonResponses;

    private int eveningResponses;

    private String bestTimeOfDay;

}
