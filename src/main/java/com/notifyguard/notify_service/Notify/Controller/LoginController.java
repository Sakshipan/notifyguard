package com.notifyguard.notify_service.Notify.Controller;

import com.notifyguard.notify_service.Notify.entity.User;
import com.notifyguard.notify_service.Notify.service.UserService;
import com.notifyguard.notify_service.Notify.jwtsecurity.JwtUtil;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.LoginResponseDto;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.LoginDto;
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
public class LoginController {
    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDTO) {
        User user = userService.validateUser(loginDTO.getEmail(), loginDTO.getPassword());
        //return new ResponseEntity<>(user, HttpStatus.OK);
        LoginResponseDto loginResponseDTO = new LoginResponseDto();

        loginResponseDTO.setToken(jwtUtil.generateToken(user));
        loginResponseDTO.setType("Bearer");
        return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);

    }

}
