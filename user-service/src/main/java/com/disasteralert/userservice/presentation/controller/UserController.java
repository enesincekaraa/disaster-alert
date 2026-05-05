package com.disasteralert.userservice.presentation.controller;

import com.disasteralert.userservice.application.dto.UserDtos;
import com.disasteralert.userservice.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDtos.UserResponse> createUser(
            @Valid @RequestBody UserDtos.CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtos.UserResponse> getUser(
            @PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<UserDtos.UserResponse> updateLocation(
            @PathVariable String id,
            @Valid @RequestBody UserDtos.UpdateLocationRequest request) {
        return ResponseEntity.ok(
                userService.updateLocation(id, request));
    }

    @PatchMapping("/{id}/fcm-token")
    public ResponseEntity<UserDtos.UserResponse> updateFcmToken(
            @PathVariable String id,
            @Valid @RequestBody UserDtos.UpdateFcmTokenRequest request) {
        return ResponseEntity.ok(
                userService.updateFcmToken(id, request));
    }

    @PatchMapping("/{id}/alert-preferences")
    public ResponseEntity<UserDtos.UserResponse> updatePreferences(
            @PathVariable String id,
            @RequestBody UserDtos.AlertPreferencesRequest request) {
        return ResponseEntity.ok(
                userService.updateAlertPreferences(id, request));
    }

    @GetMapping("/radius")
    public ResponseEntity<List<UserDtos.UserResponse>> getUsersInRadius(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "100") Double radiusKm) {
        return ResponseEntity.ok(
                userService.getUsersInRadius(
                        latitude, longitude, radiusKm));
    }
    @GetMapping("/city/{city}")
    public ResponseEntity<List<UserDtos.UserResponse>> getUsersByCity(
            @PathVariable String city) {
        return ResponseEntity.ok(
                userService.getUsersByCity(city));
    }
}
