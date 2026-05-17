package com.notifyguard.notify_service.Dashboard.Dtos;

import lombok.*;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelPerformanceResponse implements Serializable {
    private String channel;
    private long totalSent;
    private long totalDelivered;
    private long totalFailed;
}