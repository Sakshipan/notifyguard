package com.notifyguard.notify_service.Notify.Controller;

import com.notifyguard.notify_service.Notify.Dtos.RequestDto.NotificationRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.NotificationResponseDto;
import com.notifyguard.notify_service.Notify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponseDto> sendNotification(
            @RequestBody NotificationRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.sendNotification(request));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponseDto> getById(
            @PathVariable String id) {
        return ResponseEntity.ok(
                notificationService.getNotificationById(id));
    }
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDto>> getByUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/campaign/{campaignId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDto>> getByCampaign(
            @PathVariable String campaignId) {
        return ResponseEntity.ok(
                notificationService.getNotificationsByCampaign(campaignId));
    }
    @PostMapping("/{id}/respond")
    public ResponseEntity<Void> trackResponse(@PathVariable String id) {
        notificationService.trackResponse(id);
        return ResponseEntity.ok().build();
    }
}
