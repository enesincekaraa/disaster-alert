package com.disasteralert.notificationservice.infrastructure.kafka.consumer;


import com.disasteralert.notificationservice.application.service.NotificationOrchestrator;
import com.disasteralert.notificationservice.domain.event.EarthquakeAlertEvent;
import com.disasteralert.notificationservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertEventConsumer {

    private final NotificationOrchestrator orchestrator;
    public AlertEventConsumer(NotificationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }


    @KafkaListener(
            topics = KafkaTopics.ALERT_CREATED,
            groupId = "notification-service-group"
    )
    public void handleAlertCreated(EarthquakeAlertEvent event){
        log.warn("Kafka'dan alert alındı | {} | M{}",
                event.location(), event.magnitude());
        orchestrator.processAlert(event);
    }
}
