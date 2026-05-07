package com.disasteralert.alertservice.domain.event;

import java.time.LocalDateTime;

public record EarthquakeDetectedEvent(
        String id,
        LocalDateTime time,
        Double latitude,
        Double longitude,
        Double depth,
        Double magnitude,
        String location,
        String source
) {
}
