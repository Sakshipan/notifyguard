package com.notifyguard.notify_service.Notify.Controller;

import com.notifyguard.notify_service.Notify.Dtos.RequestDto.CampaignRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.CampaignResponseDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.CampaignUserResponseDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.entity.CampaignUser;
import com.notifyguard.notify_service.Notify.service.CampaignService;
import com.notifyguard.notify_service.Notify.service.CampaignUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.EnrollUserRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;
    private final CampaignUserService campaignUserService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> createCampaign(@Valid @RequestBody CampaignRequestDto registerRequestDto){
        CampaignResponseDto registerResponseDto= campaignService.createCampaign(registerRequestDto);
        return new ResponseEntity<>(registerResponseDto, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> getCampaignById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                campaignService.getCampaignById(id)
        );
    }

    @GetMapping("/getAllCampaign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CampaignResponseDto>> getCampaignAllCampaign() {

        return ResponseEntity.ok(
                campaignService.getAllCampaign()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> deleteCampaignById(@PathVariable String id){
        return ResponseEntity.ok(
                campaignService.deleteCampaignById(id)
        );

    }

    @PutMapping("/startCampaign/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> startCampaign(@PathVariable String id){
        return ResponseEntity.ok(
                campaignService.startCampaign(id)
        );
    }
    @PutMapping("/pauseCampaign/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> pauseCampaign(@PathVariable String id){
        return ResponseEntity.ok(
                campaignService.pauseCampaign(id)
        );
    }
    @PutMapping("/resumeCampaign/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> resumeCampaign(@PathVariable String id){
        return ResponseEntity.ok(
                campaignService.resumeCampaign(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignResponseDto> updateCampaign(
            @PathVariable String id,
            @Valid @RequestBody CampaignRequestDto request) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, request));
    }
//enrollUsers()
//   → add list of user ids to campaign
//   → creates CampaignUser record for each
//   → individualStatus = DISCOVERING
//

    @PostMapping("/{campaignId}/enroll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CampaignUserResponseDto>> enrollUsers(
            @PathVariable String campaignId,
            @Valid @RequestBody EnrollUserRequestDto request) {

        List<CampaignUser> campaignUsers =
                campaignUserService.enrollUsers(campaignId, request.getUserIds());

        List<CampaignUserResponseDto> response = campaignUsers.stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
    private CampaignUserResponseDto mapToResponse(CampaignUser cu) {
        CampaignUserResponseDto res = new CampaignUserResponseDto();
        res.setId(cu.getId());
        res.setUserId(cu.getUser().getId());
        res.setCampaignId(cu.getCampaign().getId());
        res.setStatus(cu.getIndividualStatus().name());
        return res;
    }
//10. getUsersInCampaign()
//    → return all CampaignUser records for a campaign

    @GetMapping("/{campaignId}/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CampaignUserResponseDto>> getUsersInCampaign(
            @PathVariable String campaignId) {
        return ResponseEntity.ok(campaignUserService.getUserinCampaign(campaignId));
    }

    @DeleteMapping("/{campaignId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeUserFromCampaign(
            @PathVariable String campaignId,
            @PathVariable String userId) {
        return ResponseEntity.ok(
                campaignUserService.removeUserFromCampaign(campaignId, userId));
    }
//
//11. removeUserFromCampaign()
//    → delete CampaignUser record

}

