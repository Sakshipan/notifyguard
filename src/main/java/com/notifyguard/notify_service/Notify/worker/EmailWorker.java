package com.notifyguard.notify_service.Notify.worker;

import com.notifyguard.notify_service.Audit.Entity.AuditLog;
import com.notifyguard.notify_service.Audit.Service.AuditService;
import com.notifyguard.notify_service.Notify.entity.*;
import com.notifyguard.notify_service.Notify.repository.DeliveryAttemptRepository;
import com.notifyguard.notify_service.Notify.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailWorker {

    private final NotificationRepository notificationRepository;
    private final DeliveryAttemptRepository deliveryAttemptRepository;
    private final AuditService auditService;

    @Transactional
    @RabbitListener(queues = "notify.email")
    public void listen(String notificationId) {

        log.info("Received EMAIL notification: {}", notificationId);

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new RuntimeException(
                        "Notification not found: " + notificationId));

        try {
            notification.setStatus(NotificationStatus.SENDING);
            notificationRepository.save(notification);

            // simulate email sending
            log.info("Sending email to: {}",
                    notification.getUser().getEmail());

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);

            DeliveryAttempt attempt = new DeliveryAttempt();
            attempt.setNotification(notification);
            attempt.setAttemptNumber(
                    deliveryAttemptRepository.countByNotification(notification) + 1
            );
            attempt.setChannel(ChannelType.EMAIL);
            attempt.setStatus(DeliveryStatus.SUCCESS);
            deliveryAttemptRepository.save(attempt);

            AuditLog auditLog = AuditLog.builder()
                    .eventType(AuditLog.EventType.NOTIFICATION_SENT)
                    .actorId("SYSTEM")
                    .actorType(AuditLog.ActorType.SYSTEM)
                    .resourceType("NOTIFICATION")
                    .resourceId(notification.getId())
                    .beforeState("status=SENDING")
                    .afterState("status=SENT")
                    .build();
            auditService.log(auditLog);

            log.info("Email sent successfully for: {}", notificationId);

        } catch (Exception ex) {

            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);

            DeliveryAttempt attempt = new DeliveryAttempt();
            attempt.setNotification(notification);
            attempt.setAttemptNumber(1);
            attempt.setChannel(ChannelType.EMAIL);
            attempt.setStatus(DeliveryStatus.FAILED);
            attempt.setErrorMessage(ex.getMessage());
            deliveryAttemptRepository.save(attempt);

            AuditLog failLog = AuditLog.builder()
                    .eventType(AuditLog.EventType.NOTIFICATION_FAILED)
                    .actorId("SYSTEM")
                    .actorType(AuditLog.ActorType.SYSTEM)
                    .resourceType("NOTIFICATION")
                    .resourceId(notification.getId())
                    .beforeState("status=SENDING")
                    .afterState("status=FAILED, error=" + ex.getMessage())
                    .build();
            auditService.log(failLog);

            log.error("Email sending failed for: {}", notificationId);

            throw ex;
        }
    }
}