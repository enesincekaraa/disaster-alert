package com.disasteralert.earthquakeservice.application.service;

import com.disasteralert.earthquakeservice.domain.model.Earthquake;
import com.disasteralert.earthquakeservice.infrastructure.kafka.producer.EarthquakeEventProducer;
import com.disasteralert.earthquakeservice.infrastructure.kandilli.KandilliClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class EarthquakeService {
    private final KandilliClient kandilliClient;
    private final EarthquakeEventProducer eventProducer;

    public EarthquakeService(KandilliClient kandilliClient, EarthquakeEventProducer eventProducer) {
        this.kandilliClient = kandilliClient;
        this.eventProducer = eventProducer;
    }


    @Value("${kandilli.min-magnitude}")
    private double minMagnitude;

    private final Set<String> processedIds = new HashSet<>();


    @PostConstruct
    public void init() {
        log.info("🌍 Earthquake Service başlatılıyor...");
        checkEarthquakes();
    }

    @Scheduled(fixedDelayString = "${kandilli.poll-interval:60000}")
    public void checkEarthquakes() {
        log.info("🔍 Deprem kontrolü yapılıyor...");

        List<Earthquake> earthquakes = kandilliClient.fetchLatestEarthquakes();

        int newCount = 0;

        for (Earthquake eq : earthquakes) {
            if (processedIds.contains(eq.getId())) continue;
            processedIds.add(eq.getId());
            if (!eq.isSignificant(minMagnitude)) continue;
            log.warn("🔴 Deprem tespit edildi! | {} | M{} | {} km | {}",
                    eq.getLocation(),
                    eq.getMagnitude(),
                    eq.getDepth(),
                    eq.getTime()
            );
            eventProducer.sendEarthquakeDetected(eq);
            newCount++;
        }
        if (newCount > 0) {
            log.info("✅ {} yeni deprem Kafka'ya gönderildi", newCount);
        } else {
            log.info("✅ Yeni deprem yok");
        }
    }

    public List<Earthquake> getLatestEarthquakes() {
        return kandilliClient.fetchLatestEarthquakes()
                .stream()
                .filter(eq -> eq.isSignificant(minMagnitude))
                .toList();
    }

}
