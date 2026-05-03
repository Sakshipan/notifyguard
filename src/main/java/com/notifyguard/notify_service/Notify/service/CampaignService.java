package com.notifyguard.notify_service.Notify.service;

import com.notifyguard.notify_service.Audit.Entity.AuditLog;
import com.notifyguard.notify_service.Audit.Service.AuditService;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.CampaignRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.CampaignResponseDto;
import com.notifyguard.notify_service.Notify.entity.Campaign;
import com.notifyguard.notify_service.Notify.entity.CampaignPhase;
import com.notifyguard.notify_service.Notify.entity.CampaignStatus;
import com.notifyguard.notify_service.Notify.repository.CampaignRepository;
import com.notifyguard.notify_service.Notify.repository.CampaignUserRepository;
import com.notifyguard.notify_service.Notify.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
// add this annotation on the class
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignUserRepository campaignUserRepository;
    private final AuditService auditService;
    private final UserRepository userRepository;
    public CampaignResponseDto createCampaign(CampaignRequestDto request) {
        Campaign campaign = new Campaign();
        campaign.setCompanyName(request.getCompanyName());
        campaign.setMessageContent(request.getMessageContent());
        campaign.setDiscoveryDays(request.getDiscoveryDays());
        campaign.setOptimizedDays(request.getOptimizedDays());
        campaign.setCycleCount(0);
        campaign.setStatus(CampaignStatus.DRAFT);
        campaign.setCurrentPhase(null);
        campaign.setStartDate(null);
        campaign.setPhaseStartedAt(null);

        Campaign saved = campaignRepository.save(campaign);

        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.CAMPAIGN_CREATED)
                .actorId(getCurrentUserId())
                .actorType(AuditLog.ActorType.USER)
                .resourceType("CAMPAIGN")
                .resourceId(saved.getId())
                .beforeState(null)
                .afterState("status=DRAFT, company=" + saved.getCompanyName())
                .build();
        auditService.log(auditLog);
        return mapToResponse(saved);
    }

        private String getCurrentUserId() {
            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();
        }
    private CampaignResponseDto mapToResponse(Campaign campaign) {
        return CampaignResponseDto.builder()
                .id(campaign.getId())
                .companyName(campaign.getCompanyName())
                .messageContent(campaign.getMessageContent())
                .discoveryDays(campaign.getDiscoveryDays())
                .optimizedDays(campaign.getOptimizedDays())
                .cycleCount(campaign.getCycleCount())
                .currentPhase(campaign.getCurrentPhase() != null
                        ? campaign.getCurrentPhase().toString()
                        : null)
                .status(campaign.getStatus().toString())
                .startDate(campaign.getStartDate())
                .phaseStartedAt(campaign.getPhaseStartedAt())
                .createdAt(campaign.getCreatedAt())
                .build();

    }

    //get campaign by id
    public CampaignResponseDto getCampaignById(String id){
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return CampaignResponseDto.builder()
                .id(campaign.getId())
                .companyName(campaign.getCompanyName())
                .messageContent(campaign.getMessageContent())
                .discoveryDays(campaign.getDiscoveryDays())
                .optimizedDays(campaign.getOptimizedDays())
                .cycleCount(campaign.getCycleCount())
                .currentPhase(campaign.getCurrentPhase() != null
                        ? campaign.getCurrentPhase().toString()
                        : null)
                .status(campaign.getStatus().toString())
                .startDate(campaign.getStartDate())
                .phaseStartedAt(campaign.getPhaseStartedAt())
                .createdAt(campaign.getCreatedAt())
                .build();
    }
//get all campaign
    public List<CampaignResponseDto> getAllCampaign() {
        return campaignRepository.findAll().stream()
                .map(campaign ->
                        CampaignResponseDto.builder()
                                .id(campaign.getId())
                                .companyName(campaign.getCompanyName())
                                .messageContent(campaign.getMessageContent())
                                .discoveryDays(campaign.getDiscoveryDays())
                                .optimizedDays(campaign.getOptimizedDays())
                                .cycleCount(campaign.getCycleCount())
                                .currentPhase(campaign.getCurrentPhase() != null
                                        ? campaign.getCurrentPhase().toString()
                                        : null)
                                .status(campaign.getStatus().toString())
                                .startDate(campaign.getStartDate())
                                .phaseStartedAt(campaign.getPhaseStartedAt())
                                .createdAt(campaign.getCreatedAt()).build())
                .toList();
    }

    //deleteById
    @Transactional
    public CampaignResponseDto deleteCampaignById(String id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campgain not found"));

        campaignRepository.delete(campaign);

        return CampaignResponseDto.builder()
                .id(campaign.getId())
                .companyName(campaign.getCompanyName())
                .messageContent(campaign.getMessageContent())
                .discoveryDays(campaign.getDiscoveryDays())
                .optimizedDays(campaign.getOptimizedDays())
                .cycleCount(campaign.getCycleCount())
                .currentPhase(campaign.getCurrentPhase() != null
                        ? campaign.getCurrentPhase().toString()
                        : null)
                .status(campaign.getStatus().toString())
                .startDate(campaign.getStartDate())
                .phaseStartedAt(campaign.getPhaseStartedAt())
                .createdAt(campaign.getCreatedAt()).build() ;
    }
//start Campaign
   public CampaignResponseDto startCampaign(String id){
        Campaign campaign=campaignRepository.findById(id).orElseThrow(()->new RuntimeException("Campaign not found"));


        if(campaign.getStatus()!=CampaignStatus.DRAFT){throw new RuntimeException("Campaign is no ton Draft Stage");

        }
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setCurrentPhase(CampaignPhase.DISCOVERY);
        campaign.setStartDate(LocalDate.now());
        campaign.setPhaseStartedAt(LocalDateTime.now());
       AuditLog auditLog = AuditLog.builder()
               .eventType(AuditLog.EventType.CAMPAIGN_PHASE_SWITCHED)
               .actorId(getCurrentUserId())
               .actorType(AuditLog.ActorType.SYSTEM)
               .resourceType("CAMPAIGN")
               .resourceId(campaign.getId())
               .beforeState(null)
               .afterState("status=ACTIVE, company=" + campaign.getCompanyName())
               .build();
       auditService.log(auditLog);
      Campaign campaign1= campaignRepository.save(campaign);
      return CampaignResponseDto.builder().id(campaign1.getId())

               .companyName(campaign1.getCompanyName())
               .messageContent(campaign1.getMessageContent())
               .discoveryDays(campaign1.getDiscoveryDays())
               .optimizedDays(campaign1.getOptimizedDays())
               .cycleCount(campaign1.getCycleCount())
               .currentPhase(campaign1.getCurrentPhase() != null
                       ? campaign1.getCurrentPhase().toString()
                       : null)
               .status(campaign1.getStatus().toString())
               .startDate(campaign1.getStartDate())
               .phaseStartedAt(campaign1.getPhaseStartedAt())
               .createdAt(campaign1.getCreatedAt()).build() ;


   }
    public CampaignResponseDto pauseCampaign(String id){
        Campaign campaign=campaignRepository.findById(id).orElseThrow(()->new RuntimeException("Campaign not found"));


        if(campaign.getStatus()!=CampaignStatus.ACTIVE){throw new RuntimeException("Campaign is no ton Draft Stage");

        }
        campaign.setStatus(CampaignStatus.PAUSED);
     campaign.setUpdatedAt(LocalDateTime.now());
        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.CAMPAIGN_PHASE_SWITCHED)
                .actorId(getCurrentUserId())
                .actorType(AuditLog.ActorType.SYSTEM)
                .resourceType("CAMPAIGN")
                .resourceId(campaign.getId())
                .beforeState(null)
                .afterState("status=PAUSED, company=" + campaign.getCompanyName())
                .build();
        auditService.log(auditLog);
        Campaign campaign1= campaignRepository.save(campaign);
        return CampaignResponseDto.builder().id(campaign1.getId())

                .companyName(campaign1.getCompanyName())
                .messageContent(campaign1.getMessageContent())
                .discoveryDays(campaign1.getDiscoveryDays())
                .optimizedDays(campaign1.getOptimizedDays())
                .cycleCount(campaign1.getCycleCount())
                .currentPhase(campaign1.getCurrentPhase() != null
                        ? campaign1.getCurrentPhase().toString()
                        : null)
                .status(campaign1.getStatus().toString())
                .startDate(campaign1.getStartDate())
                .phaseStartedAt(campaign1.getPhaseStartedAt())
                .createdAt(campaign1.getCreatedAt()).build() ;


    }
    public CampaignResponseDto resumeCampaign(String id){
        Campaign campaign=campaignRepository.findById(id).orElseThrow(()->new RuntimeException("Campaign not found"));


        if(campaign.getStatus()!=CampaignStatus.PAUSED){throw new RuntimeException("Campaign is no ton Draft Stage");

        }
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setUpdatedAt(LocalDateTime.now());
        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.CAMPAIGN_PHASE_SWITCHED)
                .actorId(getCurrentUserId())
                .actorType(AuditLog.ActorType.SYSTEM)
                .resourceType("CAMPAIGN")
                .resourceId(campaign.getId())
                .beforeState(null)
                .afterState("status=ACTIVE, company=" + campaign.getCompanyName())
                .build();
        auditService.log(auditLog);
        Campaign campaign1= campaignRepository.save(campaign);
        return CampaignResponseDto.builder().id(campaign1.getId())

                .companyName(campaign1.getCompanyName())
                .messageContent(campaign1.getMessageContent())
                .discoveryDays(campaign1.getDiscoveryDays())
                .optimizedDays(campaign1.getOptimizedDays())
                .cycleCount(campaign1.getCycleCount())
                .currentPhase(campaign1.getCurrentPhase() != null
                        ? campaign1.getCurrentPhase().toString()
                        : null)
                .status(campaign1.getStatus().toString())
                .startDate(campaign1.getStartDate())
                .phaseStartedAt(campaign1.getPhaseStartedAt())
                .createdAt(campaign1.getCreatedAt()).build() ;


    }
    //update campaign only allowed if status = DRAFT
    //→ throw if ACTIVE or PAUSED
    //→ write audit log CAMPAIGN_UPDATED
    public CampaignResponseDto updateCampaign(String id, CampaignRequestDto request) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found: " + id));

        if (campaign.getStatus() != CampaignStatus.DRAFT) {
            throw new RuntimeException("Campaign can only be updated when status is DRAFT");
        }

        // capture before state for audit
        String beforeState = "status=" + campaign.getStatus()
                + ", company=" + campaign.getCompanyName()
                + ", discoveryDays=" + campaign.getDiscoveryDays()
                + ", optimizedDays=" + campaign.getOptimizedDays();

        campaign.setCompanyName(request.getCompanyName());
        campaign.setMessageContent(request.getMessageContent());
        campaign.setDiscoveryDays(request.getDiscoveryDays());
        campaign.setOptimizedDays(request.getOptimizedDays());

        Campaign saved = campaignRepository.save(campaign);

        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.CAMPAIGN_UPDATED)
                .actorId(getCurrentUserId())
                .actorType(AuditLog.ActorType.USER)
                .resourceType("CAMPAIGN")
                .resourceId(saved.getId())
                .beforeState(beforeState)
                .afterState("status=" + saved.getStatus()
                        + ", company=" + saved.getCompanyName()
                        + ", discoveryDays=" + saved.getDiscoveryDays()
                        + ", optimizedDays=" + saved.getOptimizedDays())
                .build();
        auditService.log(auditLog);

        return mapToResponse(saved);
    }

}
