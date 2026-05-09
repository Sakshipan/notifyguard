package com.notifyguard.notify_service.Notify.Dtos.RequestDto;

import jakarta.persistence.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequestDto {

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "message_content", columnDefinition = "TEXT")
    private String messageContent;

    @Column(name = "discovery_days")
    private Integer discoveryDays;

    @Column(name = "optimized_days")
    private Integer optimizedDays;
}
