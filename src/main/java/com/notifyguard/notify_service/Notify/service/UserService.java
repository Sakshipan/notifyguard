package com.notifyguard.notify_service.Notify.service;

import com.notifyguard.notify_service.Notify.Config.PasswordConfig;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.RegisterRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.RequestDto.UpdateMyProfileDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.entity.Role;
import com.notifyguard.notify_service.Notify.entity.User;
import com.notifyguard.notify_service.Notify.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private RegisterResponseDto createUserWithRole(RegisterRequestDto request, Role role) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();

        user.setAge(request.getAge());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setQuietHoursStart(request.getQuietHoursStart());
        user.setQuietHoursEnd(request.getQuietHoursEnd());
        user.setPreferredTimezone(request.getPreferredTimezone());

        // default notification preferences
        user.setEmailEnabled(true);
        user.setSmsEnabled(true);
        user.setPushEnabled(true);
        user.setWhatsappEnabled(false);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Dynamic role
        user.setRole(role);

        userRepository.save(user);

        return RegisterResponseDto.builder()
                .id(user.getId())
                .age(user.getAge())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .quietHoursStart(user.getQuietHoursStart())
                .quietHoursEnd(user.getQuietHoursEnd())
                .build();
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
    public RegisterResponseDto registerUser(RegisterRequestDto request) {
        return createUserWithRole(request, Role.USER);
    }

    public RegisterResponseDto getMyProfile(){
        User user=getCurrentUser();
        RegisterResponseDto registerResponseDto=RegisterResponseDto.builder().id(user.getId()).age(user.getAge()).name(user.getName()).email(user.getEmail())
                .phoneNumber(user.getPhoneNumber()).quietHoursEnd(user.getQuietHoursEnd()).quietHoursStart(user.getQuietHoursStart()).build();
        return registerResponseDto;

    }
    public RegisterResponseDto createAdmin(RegisterRequestDto request) {
        return createUserWithRole(request, Role.ADMIN);
    }

    public RegisterResponseDto createAuditor(RegisterRequestDto request) {
        return createUserWithRole(request, Role.AUDITOR);
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

    //findAllUser Admin only
    public List<RegisterResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> RegisterResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .age(user.getAge())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .quietHoursStart(user.getQuietHoursStart())
                        .quietHoursEnd(user.getQuietHoursEnd())
                        .build())
                .toList();
    }
// admin
    public RegisterResponseDto getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return RegisterResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .quietHoursStart(user.getQuietHoursStart())
                .quietHoursEnd(user.getQuietHoursEnd())
                .build();
    }
//admin
    @Transactional
    public String deleteUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        return "User deleted successfully";
    }
}


