package com.notifyguard.notify_service.Notify.Dtos.RequestDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Data
@Builder
public class EnrollUserRequestDto {
    @NotEmpty(message = "User IDs list cannot be empty")
    private List<String> userIds;
}
