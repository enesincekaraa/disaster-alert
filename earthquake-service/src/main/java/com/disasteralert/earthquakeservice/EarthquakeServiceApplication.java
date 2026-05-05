package com.disasteralert.earthquakeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EarthquakeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EarthquakeServiceApplication.class, args);
    }

}
