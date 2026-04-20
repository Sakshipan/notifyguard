package com.notifyguard.notify_service.Notify.Controller;

import com.notifyguard.notify_service.Notify.Dtos.RequestDto.RegisterRequestDto;
import com.notifyguard.notify_service.Notify.Dtos.ResponseDto.RegisterResponseDto;
import com.notifyguard.notify_service.Notify.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponseDto> createAdmin(
            @Valid @RequestBody RegisterRequestDto request) {

        return new ResponseEntity<>(
                userService.createAdmin(request),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/create-auditor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponseDto> createAuditor(
            @Valid @RequestBody RegisterRequestDto request) {

        return new ResponseEntity<>(
                userService.createAuditor(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisterResponseDto>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponseDto> getUserById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                userService.deleteUserById(id)
        );
    }


}