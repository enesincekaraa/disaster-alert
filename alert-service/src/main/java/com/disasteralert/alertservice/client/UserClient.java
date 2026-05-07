package com.disasteralert.alertservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "${services.user-url}")
public interface UserClient {

    @GetMapping("/api/users/radius")
    List<UserResponse> getUsersInRadius(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radiusKm
    );


    record UserResponse(
            String id,
            String name,
            String email,
            String phoneNumber,
            String city,
            Double latitude,
            Double longitude,
            boolean earthquakeAlertsEnabled,
            boolean weatherAlertsEnabled
    ) {}
}
