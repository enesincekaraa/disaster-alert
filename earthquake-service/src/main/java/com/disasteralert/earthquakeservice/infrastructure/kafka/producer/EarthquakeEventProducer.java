package com.disasteralert.earthquakeservice.infrastructure.kafka.producer;


import com.disasteralert.earthquakeservice.domain.model.Earthquake;
import com.disasteralert.earthquakeservice.infrastructure.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EarthquakeEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public EarthquakeEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEarthquakeDetected(Earthquake earthquake){
        log.warn("🔴 Deprem Kafka'ya gönderiliyor | {} | {} | {}",
                earthquake.getLocation(),
                earthquake.getMagnitude(),
                earthquake.getTime());
        kafkaTemplate.send(KafkaTopics.EARTHQUAKE_DETECTED, earthquake.getId(), earthquake);
    }
}
