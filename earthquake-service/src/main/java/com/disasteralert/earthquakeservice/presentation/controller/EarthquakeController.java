package com.disasteralert.earthquakeservice.presentation.controller;

import com.disasteralert.earthquakeservice.application.service.EarthquakeService;
import com.disasteralert.earthquakeservice.domain.model.Earthquake;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
public class EarthquakeController {
    private final EarthquakeService earthquakeService;

    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }

    @GetMapping
    public ResponseEntity<List<Earthquake>> getLatestEarthquakes() {
        return ResponseEntity.ok(
                earthquakeService.getLatestEarthquakes());
    }
    @PostMapping("/check")
    public ResponseEntity<String> triggerCheck() {
        earthquakeService.checkEarthquakes();
        return ResponseEntity.ok("Deprem kontrolü tetiklendi");
    }
}
