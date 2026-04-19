package com.notifyguard.notify_service.Notify.service;

import com.notifyguard.notify_service.Notify.Config.PasswordConfig;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.RegisterRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.UpdateMyProfileDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.entity.Role;
import com.notifyguard.notify_service.Notify.entity.User;
import com.notifyguard.notify_service.Notify.repository.UserRepository;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto){

    // cleaner approach
    if(userRepository.existsByEmail(registerRequestDto.getEmail())){
        throw new RuntimeException("user already exists");
    }
        User user1=new User();
        user1.setAge(registerRequestDto.getAge());
        user1.setName(registerRequestDto.getName());
        user1.setEmail(registerRequestDto.getEmail());
        user1.setPhoneNumber(registerRequestDto.getPhoneNumber());
        user1.setQuietHoursStart(registerRequestDto.getQuietHoursStart());
        user1.setQuietHoursEnd(registerRequestDto.getQuietHoursEnd());
        user1.setPreferredTimezone(registerRequestDto.getPreferredTimezone());
    // these four lines are still not there
    user1.setEmailEnabled(true);
    user1.setSmsEnabled(true);
    user1.setPushEnabled(true);
    user1.setWebhookEnabled(false);
user1.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
user1.setRole(Role.USER);
userRepository.save(user1);
RegisterResponseDto registerResponseDto=RegisterResponseDto.builder().id(user1.getId()).age(user1.getAge()).name(user1.getName()).email(user1.getEmail())
        .phoneNumber(user1.getPhoneNumber()).quietHoursEnd(user1.getQuietHoursEnd()).quietHoursStart(user1.getQuietHoursStart()).build();
return registerResponseDto;
    }

    public User validateUser(String email, String password){
        User user= userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException("wrong password");
        }else{
            return user;
        }

    }
    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public RegisterResponseDto getMyProfile(){
        User user=getCurrentUser();
        RegisterResponseDto registerResponseDto=RegisterResponseDto.builder().id(user.getId()).age(user.getAge()).name(user.getName()).email(user.getEmail())
                .phoneNumber(user.getPhoneNumber()).quietHoursEnd(user.getQuietHoursEnd()).quietHoursStart(user.getQuietHoursStart()).build();
        return registerResponseDto;

    }
    public RegisterResponseDto updateMyProfile(UpdateMyProfileDto profileDto){
    User user = getCurrentUser();
    user.setName(profileDto.getName());
    user.setPhoneNumber(profileDto.getPhoneNumber());
    user.setAge(profileDto.getAge());
    user.setPreferredTimezone(profileDto.getPreferredTimezone());

    userRepository.save(user);
        RegisterResponseDto registerResponseDto=RegisterResponseDto.builder().id(user.getId()).age(user.getAge()).name(user.getName()).email(user.getEmail())
                .phoneNumber(user.getPhoneNumber()).quietHoursEnd(user.getQuietHoursEnd()).quietHoursStart(user.getQuietHoursStart()).build();
        return registerResponseDto;


    }

    public void deleteMyAccount() {

        User user = getCurrentUser();

        userRepository.delete(user);
    }
}


