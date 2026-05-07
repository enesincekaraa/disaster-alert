package com.disasteralert.alertservice.infrastructure.kafka.producer;

import com.disasteralert.alertservice.domain.event.AlertEvent;
import com.disasteralert.alertservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public AlertEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAlertCreated(AlertEvent.EarthquakeAlert alert){
        log.warn("🚨 Alert Kafka'ya gönderiliyor | {} | M{} | {} kullanıcı",
                alert.location(),
                alert.magnitude(),
                alert.targetUserIds().size());

        kafkaTemplate.send(
                KafkaTopics.ALERT_CREATED,
                alert.alertId(),
                alert
        );
    }


}
