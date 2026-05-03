package com.notifyguard.notify_service.Notify.Dtos.ResponseDto;

import com.notifyguard.notify_service.Notify.entity.CampaignPhase;
import com.notifyguard.notify_service.Notify.entity.CampaignStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponseDto {
    private String id;

    private String companyName;

    private String messageContent;

    private Integer discoveryDays;

    private Integer optimizedDays;

    private Integer cycleCount;

    private String currentPhase;

    private String status;

    private LocalDate startDate;

    private LocalDateTime phaseStartedAt;

    private LocalDateTime createdAt;


}
