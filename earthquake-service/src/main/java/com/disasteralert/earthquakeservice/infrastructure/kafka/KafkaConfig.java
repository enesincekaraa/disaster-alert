package com.disasteralert.earthquakeservice.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic earthquakeDetectedTopic() {
        return TopicBuilder.name(KafkaTopics.EARTHQUAKE_DETECTED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
