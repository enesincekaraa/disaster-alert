package com.disasteralert.notificationservice.application.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${twilio.phone-number}")
    private String fromNumber;

    public void sendSms(String toNumber, String messageText) {
        try {
            log.info("📱 SMS gönderiliyor: {}", toNumber);

            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    messageText
            ).create();

            log.info("✅ SMS gönderildi | SID: {} | Durum: {}",
                    message.getSid(), message.getStatus());

        } catch (Exception e) {
            log.error("❌ SMS gönderilemedi: {} | Hata: {}",
                    toNumber, e.getMessage());
        }
    }

    public String buildEarthquakeMessage(
            String location,
            Double magnitude,
            String severity,
            String city) {

        String severityTr = switch (severity) {
            case "LOW"      -> "Düşük";
            case "MEDIUM"   -> "Orta";
            case "HIGH"     -> "Yüksek";
            case "CRITICAL" -> "KRİTİK";
            default         -> severity;
        };

        return String.format(
                "🔴 DEPREM UYARISI\n" +
                        "Bölge: %s\n" +
                        "Büyüklük: M%.1f\n" +
                        "Şiddet: %s\n" +
                        "Konumunuz (%s) etki alanındadır.\n" +
                        "Güvende kalın!",
                location, magnitude, severityTr, city
        );
    }
}