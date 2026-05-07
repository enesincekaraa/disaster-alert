package com.disasteralert.notificationservice.domain.event;

import java.time.LocalDateTime;
import java.util.List;

public record EarthquakeAlertEvent(
        String alertId,
        String earthquakeId,
        Double magnitude,
        String location,
        Double latitude,
        Double longitude,
        Double depth,
        Double radiusKm,
        String severity,
        List<String> targetUserIds,
        LocalDateTime createdAt
) {}