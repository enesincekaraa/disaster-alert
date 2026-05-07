package com.disasteralert.alertservice.infrastructure.kafka.consumer;

import com.disasteralert.alertservice.domain.event.EarthquakeDetectedEvent;
import com.disasteralert.alertservice.infrastructure.kafka.KafkaTopics;
import com.disasteralert.alertservice.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EarthquakeEventConsumer {
    private final AlertService alertService;

    public EarthquakeEventConsumer(AlertService alertService) {
        this.alertService = alertService;
    }

    @KafkaListener(
            topics = KafkaTopics.EARTHQUAKE_DETECTED,
            groupId = "alert-service-group"
    )
    public void handleEarthquakeDetected(EarthquakeDetectedEvent event) {
        log.warn("Kafka'dan deprem event'i alındı | {} | M{}",
                event.location(), event.magnitude());
        alertService.processEarthquake(event);
    }
}
