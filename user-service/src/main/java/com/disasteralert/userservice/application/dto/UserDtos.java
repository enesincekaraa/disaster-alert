package com.disasteralert.userservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UserDtos {
    public record CreateUserRequest(
            @NotBlank String name,
            @NotBlank String email,
            @NotBlank String phoneNumber,
            @NotBlank String city,
            @NotNull Double latitude,
            @NotNull Double longitude
    ){}

    public record UpdateLocationRequest(
            @NotBlank String city,
            @NotNull Double latitude,
            @NotNull Double longitude
    ) {}

    public record UpdateFcmTokenRequest(
            @NotBlank String fcmToken
    ) {}

    public record AlertPreferencesRequest(
            boolean earthquakeAlerts,
            boolean weatherAlerts
    ) {}

    public record UserResponse(
            String id,
            String name,
            String email,
            String phoneNumber,
            String city,
            Double latitude,
            Double longitude,
            boolean earthquakeAlertsEnabled,
            boolean weatherAlertsEnabled,
            LocalDateTime createdAt
    ) {}
}
