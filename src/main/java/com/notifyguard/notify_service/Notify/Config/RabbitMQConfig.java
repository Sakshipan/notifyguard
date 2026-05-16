package com.notifyguard.notify_service.Notify.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        // This listener forces declaration as soon as the connection opens
        ((AbstractConnectionFactory) connectionFactory)
                .addConnectionListener(connection -> {
                    System.out.println("RabbitMQ connected! Declaring exchanges/queues...");
                });

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;  // remove afterPropertiesSet() — it doesn't help here
    }
    @Bean
    public ApplicationRunner forceRabbitConnection(RabbitTemplate rabbitTemplate) {
        return args -> {
            rabbitTemplate.execute(channel -> {
                System.out.println("RabbitMQ connected!");
                return null;
            });
        };
    }
    @PostConstruct
    public void init() {
        System.out.println("RabbitMQConfig Loaded");
    }
//exchane
    public static final String NOTIFICATION_EXCHANGE =
            "notifyguard.notifications";
//rounting keys
    public static final String EMAIL_ROUTING_KEY =
            "notification.EMAIL";

    public static final String SMS_ROUTING_KEY =
            "notification.SMS";

    public static final String PUSH_ROUTING_KEY =
            "notification.PUSH_NOTIFICATION";

    public static final String WHATSAPP_ROUTING_KEY =
            "notification.WHATSAPP";
    // QUEUES

    public static final String EMAIL_QUEUE =
            "notify.email";

    public static final String SMS_QUEUE =
            "notify.sms";

    public static final String PUSH_QUEUE =
            "notify.push";

    public static final String WHATSAPP_QUEUE =
            "notify.whatsapp";

    public static final String DEAD_LETTER_QUEUE =
            "notify.dlq";


    // EXCHANGE

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }


    // DEAD LETTER QUEUE

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    // =========================================
    // EMAIL QUEUE
    // =========================================

    @Bean
    public Queue emailQueue() {
        return QueueBuilder
                .durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument(
                        "x-dead-letter-routing-key",
                        DEAD_LETTER_QUEUE
                )
                .build();
    }


    // SMS QUEUE

    @Bean
    public Queue smsQueue() {
        return QueueBuilder
                .durable(SMS_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument(
                        "x-dead-letter-routing-key",
                        DEAD_LETTER_QUEUE
                )
                .build();
    }

    // =========================================
    // PUSH QUEUE
    // =========================================

    @Bean
    public Queue pushQueue() {
        return QueueBuilder
                .durable(PUSH_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument(
                        "x-dead-letter-routing-key",
                        DEAD_LETTER_QUEUE
                )
                .build();
    }

    // =========================================
    // WHATSAPP QUEUE
    // =========================================

    @Bean
    public Queue whatsappQueue() {
        return QueueBuilder
                .durable(WHATSAPP_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument(
                        "x-dead-letter-routing-key",
                        DEAD_LETTER_QUEUE
                )
                .build();
    }


    // BINDINGS

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(notificationExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding smsBinding() {
        return BindingBuilder
                .bind(smsQueue())
                .to(notificationExchange())
                .with(SMS_ROUTING_KEY);
    }

    @Bean
    public Binding pushBinding() {
        return BindingBuilder
                .bind(pushQueue())
                .to(notificationExchange())
                .with(PUSH_ROUTING_KEY);
    }

    @Bean
    public Binding whatsappBinding() {
        return BindingBuilder
                .bind(whatsappQueue())
                .to(notificationExchange())
                .with(WHATSAPP_ROUTING_KEY);
    }


    // JSON MESSAGE CONVERTER

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
