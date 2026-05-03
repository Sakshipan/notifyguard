package com.notifyguard.notify_service.Notify.Dtos.ResponseDto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignUserResponseDto {

    private String id;
    private String userId;
    private String campaignId;
    private String status;
}