package com.notifyguard.notify_service.Notify.Dtos.ResponseDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String type;

}
