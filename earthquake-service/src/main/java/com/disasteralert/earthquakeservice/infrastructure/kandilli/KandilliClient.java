package com.disasteralert.earthquakeservice.infrastructure.kandilli;

import com.disasteralert.earthquakeservice.domain.model.Earthquake;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KandilliClient {

    @Value("${kandilli.url}")
    private String kandilliUrl;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    public List<Earthquake> fetchLatestEarthquakes() {
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            log.info("Kandilli'den deprem verisi çekiliyor...");

            Document doc = Jsoup.connect(kandilliUrl)
                    .timeout(10000)
                    .get();

            Element pre = doc.select("pre").first();
            if (pre == null) {
                log.warn("Kandilli sayfasında <pre> bulunamadı");
                return earthquakes;
            }

            String rawHtml = pre.html();
            rawHtml = rawHtml.replaceAll("<br[^>]*>", "\n");
            String rawData = rawHtml.replaceAll("<[^>]+>", "");

            String[] lines = rawData.split("\n");

            for (int i = 0; i < Math.min(5, lines.length); i++) {
                log.info("Satır {}: '{}'", i, lines[i].trim());
            }

            for (String line : lines) {
                line = line.trim();

                if (line.isEmpty()) continue;
                if (line.startsWith("Magnitude")) continue;
                if (line.startsWith("Date")) continue;
                if (line.startsWith("---")) continue;

                Earthquake eq = parseLine(line);
                if (eq != null) earthquakes.add(eq);
            }

            log.info("Kandilli'den {} deprem verisi alındı",
                    earthquakes.size());

        } catch (Exception e) {
            log.error("Kandilli bağlantı hatası: {}", e.getMessage());
        }

        return earthquakes;
    }

    private Earthquake parseLine(String line) {
        try {
            line = line.replaceAll("(?i)\\s+(quick|revised|manual).*$", "").trim();

            String[] parts = line.split("\\s+");
            if (parts.length < 9) return null;

            String dateTime = parts[0] + " " + parts[1];
            LocalDateTime time = LocalDateTime.parse(dateTime, FORMATTER);

            double latitude  = Double.parseDouble(parts[2]);
            double longitude = Double.parseDouble(parts[3]);
            double depth     = Double.parseDouble(parts[4]);

            double magnitude = parseMagnitude(parts[6]); // ML önce
            if (magnitude <= 0) magnitude = parseMagnitude(parts[7]); // Mw
            if (magnitude <= 0) magnitude = parseMagnitude(parts[5]); // MD
            if (magnitude <= 0) return null;

            StringBuilder location = new StringBuilder();
            for (int i = 8; i < parts.length; i++) {
                location.append(parts[i]).append(" ");
            }

            String id = dateTime.replaceAll("[^0-9]", "")
                    + "_" + parts[2] + "_" + parts[3];

            return Earthquake.builder()
                    .id(id)
                    .time(time)
                    .latitude(latitude)
                    .longitude(longitude)
                    .depth(depth)
                    .magnitude(magnitude)
                    .location(location.toString().trim())
                    .source("Kandilli")
                    .build();

        } catch (Exception e) {
            log.debug("Parse hatası — satır: '{}' | hata: {}",
                    line, e.getMessage());
            return null;
        }
    }

    private double parseMagnitude(String value) {
        try {
            if (value.equals("-.-") || value.equals("---")) return 0;
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}