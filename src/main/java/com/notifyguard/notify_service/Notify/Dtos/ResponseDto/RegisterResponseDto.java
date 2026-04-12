package com.notifyguard.notify_service.Notify.Dtos.ResponseDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import java.time.LocalTime;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDto {
    private String id;


    private String name;


    private Integer age;


    private String email;


    private String phoneNumber;


    private LocalTime quietHoursStart;


    private LocalTime quietHoursEnd;
}
