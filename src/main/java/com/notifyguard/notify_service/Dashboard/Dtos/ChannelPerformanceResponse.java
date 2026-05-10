package com.notifyguard.notify_service.Dashboard.Dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelPerformanceResponse {
    private String channel;
    private long totalSent;
    private long totalDelivered;
    private long totalFailed;
}