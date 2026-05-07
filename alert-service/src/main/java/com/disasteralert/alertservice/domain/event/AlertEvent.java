package com.disasteralert.alertservice.domain.event;

import java.time.LocalDateTime;
import java.util.List;

public sealed interface AlertEvent permits AlertEvent.EarthquakeAlert {

    record EarthquakeAlert(
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
            LocalDateTime createdAt) implements AlertEvent {
    }

}
