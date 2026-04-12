package com.notifyguard.notify_service.Notify.Dtos.RequestDto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

private String Password;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits"
    )
    private String phoneNumber;


    private LocalTime quietHoursStart;

    private LocalTime quietHoursEnd;
    private String preferredTimezone ;
}