package com.notifyguard.notify_service.Notify.service;

import com.notifyguard.notify_service.Notify.Config.PasswordConfig;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.RegisterRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.entity.User;
import com.notifyguard.notify_service.Notify.repository.UserRepository;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto){
    Optional<User> user=userRepository.findByEmail(registerRequestDto.getEmail());
    if(!user.isEmpty()){
        new RuntimeException("user already exists");
    }
        User user1=new User();
        user1.setAge(registerRequestDto.getAge());
        user1.setName(registerRequestDto.getName());
        user1.setEmail(registerRequestDto.getEmail());
        user1.setPhoneNumber(registerRequestDto.getPhoneNumber());
        user1.setQuietHoursStart(registerRequestDto.getQuietHoursStart());
        user1.setQuietHoursEnd(registerRequestDto.getQuietHoursEnd());
        user1.setPreferredTimezone(registerRequestDto.getPreferredTimezone());
user1.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
userRepository.save(user1);
RegisterResponseDto registerResponseDto=RegisterResponseDto.builder().id(user1.getId()).age(user1.getAge()).name(user1.getName()).email(user1.getEmail())
        .phoneNumber(user1.getPhoneNumber()).quietHoursEnd(user1.getQuietHoursEnd()).quietHoursStart(user1.getQuietHoursStart()).build();
return registerResponseDto;
    }
}


