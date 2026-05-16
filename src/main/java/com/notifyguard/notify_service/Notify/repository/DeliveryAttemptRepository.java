package com.notifyguard.notify_service.Notify.repository;

import com.notifyguard.notify_service.Notify.entity.DeliveryAttempt;
import com.notifyguard.notify_service.Notify.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAttemptRepository extends JpaRepository<DeliveryAttempt,String> {
    int countByNotification(Notification notification);
}
