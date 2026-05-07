package com.disasteralert.alertservice.service;


import com.disasteralert.alertservice.client.UserClient;
import com.disasteralert.alertservice.domain.event.AlertEvent;
import com.disasteralert.alertservice.domain.event.EarthquakeDetectedEvent;
import com.disasteralert.alertservice.infrastructure.kafka.producer.AlertEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AlertService {
    private final UserClient userClient;
    private final AlertEventProducer alertProducer;

    public AlertService(UserClient userClient, AlertEventProducer alertProducer) {
        this.userClient = userClient;
        this.alertProducer = alertProducer;
    }

    @Value("${alert.radius.below-3:50}")
    private double radiusBelow3;

    @Value("${alert.radius.between-3-4:100}")
    private double radiusBetween3And4;

    @Value("${alert.radius.between-4-5:200}")
    private double radiusBetween4And5;

    @Value("${alert.radius.between-5-6:350}")
    private double radiusBetween5And6;

    @Value("${alert.radius.above-6:500}")
    private double radiusAbove6;


    public void processEarthquake(EarthquakeDetectedEvent event){
        log.warn("⚡ Deprem işleniyor | {} | M{} | {} km",
                event.location(), event.magnitude(), event.depth());


        //Şiddete göre etki yarıçapı

        double radius = calculateRadius(event.magnitude());

        String severity = calculateSeverity(event.magnitude());

        log.info("📍 Etki yarıçapı: {} km | Şiddet: {}",
                radius, severity);

        List<String> targetUserIds;

        try {
            List<UserClient.UserResponse> users=userClient.getUsersInRadius(
                    event.latitude(), event.longitude(), radius
            );
            targetUserIds = users.stream()
                    .filter(UserClient.UserResponse::earthquakeAlertsEnabled)
                    .map(UserClient.UserResponse::id)
                    .toList();
            log.info("📍 Etki yarıçapı: {} km | Şiddet: {}",
                    radius, severity);
        }catch (Exception e){
            log.error("User service bağlantı hatası: {}", e.getMessage());

            targetUserIds = List.of();
        }

        if (targetUserIds.isEmpty() && !severity.equals("HIGH")&&!severity.equals("CRITICAL"))
        {
            log.info("Etkilenen kullanıcı yok, uyarı gönderilmedi.");

            return;
        }

        AlertEvent.EarthquakeAlert alert= new AlertEvent.EarthquakeAlert(
                UUID.randomUUID().toString(),
                event.id(),
                event.magnitude(),
                event.location(),
                event.latitude(),
                event.longitude(),
                event.depth(),
                radius,
                severity,
                targetUserIds,
                LocalDateTime.now()
        );
        alertProducer.sendAlertCreated(alert);
        log.warn("✅ Uyarı oluşturuldu | Şiddet: {} | {} kullanıcı bildirilecek",
                severity, targetUserIds.size());

    }


    private double calculateRadius(double magnitude) {
        if (magnitude < 3.0) return radiusBelow3;
        if (magnitude < 4.0) return radiusBetween3And4;
        if (magnitude < 5.0) return radiusBetween4And5;
        if (magnitude < 6.0) return radiusBetween5And6;
        return radiusAbove6;
    }

    private String calculateSeverity(double magnitude) {
        if (magnitude < 3.0) return "LOW";
        if (magnitude < 4.0) return "MEDIUM";
        if (magnitude < 5.0) return "HIGH";
        return "CRITICAL";
    }
}
