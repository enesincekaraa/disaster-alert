package com.disasteralert.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-client", url = "${services.user-url}")

public interface UserClient {


    @GetMapping("/api/users/{id}")
    UserInfo getUser(@PathVariable String id);

     record UserInfo(
            String id,
            String name,
            String phoneNumber,
            String email,
            String city,
            String fcmToken
    ) {}

}
