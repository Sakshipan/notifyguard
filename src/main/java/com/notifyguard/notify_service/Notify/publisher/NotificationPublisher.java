package com.notifyguard.notify_service.Notify.publisher;
import com.notifyguard.notify_service.Notify.Config.RabbitMQConfig;
import com.notifyguard.notify_service.Notify.entity.Notification;
import com.notifyguard.notify_service.Notify.entity.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(Notification notification) {

        String routingKey = getRoutingKey(notification.getChannel());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                routingKey,
                notification.getId()
        );

        System.out.println(
                "Notification published: " + notification.getId()
        );
    }

    private String getRoutingKey(ChannelType channel) {

        return switch (channel) {

            case EMAIL ->
                    RabbitMQConfig.EMAIL_ROUTING_KEY;

            case SMS ->
                    RabbitMQConfig.SMS_ROUTING_KEY;

            case PUSH_NOTIFICATION ->
                    RabbitMQConfig.PUSH_ROUTING_KEY;

            case WHATSAPP ->
                    RabbitMQConfig.WHATSAPP_ROUTING_KEY;
        };
    }
}