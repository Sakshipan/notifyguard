package com.notifyguard.notify_service.Dashboard.Controller;

import com.notifyguard.notify_service.Dashboard.Dtos.*;
import com.notifyguard.notify_service.Dashboard.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // GET /api/dashboard/campaign/{id}/summary
    @GetMapping("/campaign/{id}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<CampaignSummaryResponse> getCampaignSummary(
            @PathVariable String id) {
        return ResponseEntity.ok(dashboardService.getCampaignSummary(id));
    }

    // GET /api/dashboard/campaign/{id}/users
    @GetMapping("/campaign/{id}/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserCampaignBreakdownResponse>> getUsersInCampaign(
            @PathVariable String id) {
        return ResponseEntity.ok(dashboardService.getUsersInCampaignBreakdown(id));
    }

    // GET /api/dashboard/channel-performance
    @GetMapping("/channel-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ChannelPerformanceResponse>> getChannelPerformance() {
        return ResponseEntity.ok(dashboardService.getChannelPerformance());
    }

    // GET /api/dashboard/users/{userId}/history
    @GetMapping("/users/{userId}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserNotificationHistoryResponse>> getUserHistory(
            @PathVariable String userId) {
        return ResponseEntity.ok(dashboardService.getUserNotificationHistory(userId));
    }

    // GET /api/dashboard/unresponsive-users
    @GetMapping("/unresponsive-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UserCampaignBreakdownResponse>> getUnresponsiveUsers() {
        return ResponseEntity.ok(dashboardService.getUnresponsiveUsers());
    }
}
