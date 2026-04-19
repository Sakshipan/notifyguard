package com.notifyguard.notify_service.Notify.Controller;

import com.notifyguard.notify_service.Notify.Dtos.RequestDto.LoginDto;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.RegisterRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.UpdateMyProfileDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.LoginResponseDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.jwtsecurity.JwtUtil;
import com.notifyguard.notify_service.Notify.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto){
       RegisterResponseDto registerResponseDto= userService.registerUser(registerRequestDto);
       return new ResponseEntity<>(registerResponseDto, HttpStatus.CREATED);
    }
    @GetMapping("/me")
    public ResponseEntity<RegisterResponseDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<RegisterResponseDto> updateMyProfile(
            @RequestBody UpdateMyProfileDto request) {

        return ResponseEntity.ok(
                userService.updateMyProfile(request)
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount() {

        userService.deleteMyAccount();

        return ResponseEntity.ok("Account deleted successfully");
    }

}
