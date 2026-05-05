package com.disasteralert.userservice.application.service;

import com.disasteralert.userservice.application.dto.UserDtos;
import com.disasteralert.userservice.domain.model.User;
import com.disasteralert.userservice.infrastructure.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDtos.UserResponse createUser(UserDtos.CreateUserRequest req){
        if (userRepository.findByEmail(req.email()).isPresent())
            throw new IllegalArgumentException("Bu email zaten kayıtlı: " + req.email());

        User user = User.create(
                req.name(),
                req.email(),
                req.phoneNumber(),
                req.city(),
                req.latitude(),
                req.longitude()
        );
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserDtos.UserResponse updateLocation(String userId, UserDtos.UpdateLocationRequest req){
        User user = findById(userId);
        user.updateLocation(
                req.city(),
                req.latitude(),
                req.longitude()
        );
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserDtos.UserResponse updateFcmToken(String userId, UserDtos.UpdateFcmTokenRequest req){
        User user = findById(userId);
        user.updateFcmToken(req.fcmToken());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserDtos.UserResponse updateAlertPreferences(
            String userId, UserDtos.AlertPreferencesRequest request) {
        User user = findById(userId);
        user.updateAlertPreferences(
                request.earthquakeAlerts(),
                request.weatherAlerts()
        );
        return toResponse(userRepository.save(user));
    }


    @Transactional(readOnly = true)
    public UserDtos.UserResponse getUser(String userId) {
        return toResponse(findById(userId));
    }

    @Transactional(readOnly = true)
    public List<UserDtos.UserResponse> getUsersInRadius(
            Double latitude, Double longitude, Double radiusKm) {
        return userRepository
                .findUsersInRadius(latitude, longitude, radiusKm)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserDtos.UserResponse> getUsersByCity(String city) {
        return userRepository.findByCity(city)
                .stream()
                .map(this::toResponse)
                .toList();
    }




    private User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("Kullanıcı bulunamadı: " + userId)
        );
    }

    private UserDtos.UserResponse toResponse(User user) {
        return new UserDtos.UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCity(),
                user.getLatitude(),
                user.getLongitude(),
                user.isEarthquakeAlertsEnabled(),
                user.isWeatherAlertsEnabled(),
                user.getCreatedAt()
        );
    }
}
