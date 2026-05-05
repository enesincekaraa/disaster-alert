package com.disasteralert.userservice.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;


    private boolean earthquakeAlertsEnabled;
    private boolean weatherAlertsEnabled;

    private String fcmToken;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    public static User create(
            String name,
            String email,
            String phoneNumber,
            String city,
            Double latitude,
            Double longitude
    ){
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.name = name;
        user.email = email;
        user.phoneNumber = phoneNumber;
        user.city = city;
        user.latitude = latitude;
        user.longitude = longitude;
        user.earthquakeAlertsEnabled = true;
        user.weatherAlertsEnabled = true;
        user.createdAt = LocalDateTime.now();
        return user;
    }

    public void updateLocation(String city,Double latitude,Double longitude){
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void updateAlertPreferences(boolean earthquake,boolean weather){
        this.earthquakeAlertsEnabled = earthquake;
        this.weatherAlertsEnabled = weather;
    }



}
