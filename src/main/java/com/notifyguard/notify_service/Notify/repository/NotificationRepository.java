package com.notifyguard.notify_service.Notify.repository;

import com.notifyguard.notify_service.Notify.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}
