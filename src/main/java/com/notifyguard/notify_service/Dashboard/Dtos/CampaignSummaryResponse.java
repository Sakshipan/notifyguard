package com.notifyguard.notify_service.Dashboard.Dtos;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignSummaryResponse implements Serializable {
    private String campaignId;
    private String companyName;
    private String status;
    private String currentPhase;
    private Integer cycleCount;
    private long totalNotificationsSent;
    private long totalUsersTargeted;
}
