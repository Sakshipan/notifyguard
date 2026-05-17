package com.notifyguard.notify_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@EnableCaching
public class NotifyServiceApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(NotifyServiceApplication.class, args);

        System.out.println("\n=== REGISTERED BEANS ===");
        Arrays.stream(ctx.getBeanDefinitionNames())
                .sorted()
                .forEach(System.out::println);
        System.out.println("=== END BEANS ===\n");
    }
}