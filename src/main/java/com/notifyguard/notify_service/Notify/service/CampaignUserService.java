package com.notifyguard.notify_service.Notify.service;

import com.notifyguard.notify_service.Audit.Entity.AuditLog;
import com.notifyguard.notify_service.Audit.Service.AuditService;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.CampaignUserResponseDto;
import com.notifyguard.notify_service.Notify.entity.*;
import com.notifyguard.notify_service.Notify.repository.CampaignRepository;
import com.notifyguard.notify_service.Notify.repository.CampaignUserRepository;
import com.notifyguard.notify_service.Notify.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CampaignUserService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CampaignUserRepository campaignUserRepository;
    private final AuditService auditService;

    @Transactional
    public List<CampaignUser> enrollUsers(String campaignId, List<String> userIds) {

        // 1. Fetch Campaign
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        // 2. Fetch all users in one go (optimized)
        List<User> users = userRepository.findAllById(userIds);

        if (users.size() != userIds.size()) {
            throw new RuntimeException("Some users not found");
        }

        List<CampaignUser> campaignUsers = new ArrayList<>();

        // 3. Create CampaignUser entries
        for (User user : users) {

            // Optional: prevent duplicate enrollment
            boolean exists = campaignUserRepository
                    .existsByCampaignIdAndUserId(campaignId, user.getId());

            if (exists) continue;

            CampaignUser campaignUser = new CampaignUser();
            campaignUser.setCampaign(campaign);
            campaignUser.setUser(user);
            campaignUser.setIndividualStatus(CampaignUserStatus.DISCOVERING);
            campaignUser.setBestChannel(null);
            campaignUser.setMessagesSentCount(0);
            campaignUser.setMessagesResponded(0);

            campaignUsers.add(campaignUser);
        }

        return campaignUserRepository.saveAll(campaignUsers);
    }

    @Transactional
    public List<CampaignUserResponseDto> getUserinCampaign(String campaignId){
        campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException(
                        "Campaign not found: " + campaignId));

        return campaignUserRepository.findByCampaignId(campaignId)
                .stream()
                .map(this::mapToCampaignUserResponse)
                .toList();
    }
    @Transactional
    public String removeUserFromCampaign(String campaignId, String userId) {

        campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException(
                        "Campaign not found: " + campaignId));

        CampaignUser campaignUser = campaignUserRepository
                .findByCampaignIdAndUserId(campaignId, userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not enrolled in this campaign"));

        campaignUserRepository.delete(campaignUser);

        AuditLog auditLog = AuditLog.builder()
                .eventType(AuditLog.EventType.USER_OPT_OUT)
                .actorId(getCurrentUserId())
                .actorType(AuditLog.ActorType.USER)
                .resourceType("CAMPAIGN_USER")
                .resourceId(campaignUser.getId())
                .beforeState("userId=" + userId
                        + ", campaignId=" + campaignId)
                .afterState("removed")
                .build();
        auditService.log(auditLog);

        return "User removed from campaign successfully";
    }

    private CampaignUserResponseDto mapToCampaignUserResponse(
            CampaignUser campaignUser) {
        return CampaignUserResponseDto.builder()
                .id(campaignUser.getId())
                .userId(campaignUser.getUser().getId())
                .campaignId(campaignUser.getCampaign().getId())
                .status(campaignUser.getIndividualStatus() != null
                        ? campaignUser.getIndividualStatus().toString()
                        : null)
                .build();
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
}