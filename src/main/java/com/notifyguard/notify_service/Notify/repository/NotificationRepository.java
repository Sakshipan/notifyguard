package com.notifyguard.notify_service.Notify.repository;

import com.notifyguard.notify_service.Notify.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByCampaignId(String campaignId);
}
