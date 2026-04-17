package com.notifyguard.notify_service.Notify.Dtos.RequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String Password;
}
