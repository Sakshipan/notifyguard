package com.notifyguard.notify_service.Notify.Controller;

import com.notifyguard.notify_service.Notify.Dtos.RequestDto.RegisterRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto){
       RegisterResponseDto registerResponseDto= userService.registerUser(registerRequestDto);
       return new ResponseEntity<>(registerResponseDto, HttpStatus.CREATED);
    }

}
