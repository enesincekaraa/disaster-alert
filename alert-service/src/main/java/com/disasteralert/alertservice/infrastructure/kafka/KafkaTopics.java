package com.disasteralert.alertservice.infrastructure.kafka;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String EARTHQUAKE_DETECTED = "earthquake-detected";
    public static final String ALERT_CREATED       = "alert-created";


}
