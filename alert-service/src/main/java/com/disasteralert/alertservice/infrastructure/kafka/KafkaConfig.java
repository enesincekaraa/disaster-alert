package com.disasteralert.alertservice.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic alertCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.ALERT_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
