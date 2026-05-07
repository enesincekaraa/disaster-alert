package com.disasteralert.notificationservice.application.service;

import com.disasteralert.notificationservice.client.UserClient;
import com.disasteralert.notificationservice.domain.event.EarthquakeAlertEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationOrchestrator {
    private final UserClient userClient;
    private final SmsService smsService;
    private final PushNotificationService pushService;

    public NotificationOrchestrator(UserClient userClient, SmsService smsService, PushNotificationService pushService) {
        this.userClient = userClient;
        this.smsService = smsService;
        this.pushService = pushService;
    }

    public void processAlert(EarthquakeAlertEvent alert){
        log.warn("🚨 Uyarı işleniyor | {} | M{} | {} kullanıcı",
                alert.location(),
                alert.magnitude(),
                alert.targetUserIds().size());

        alert.targetUserIds().forEach(userId-> notifyUser(userId,alert));
        log.info("✅ Tüm bildirimler gönderildi | alert: {}",
                alert.alertId());
    }

    private void notifyUser(String userId, EarthquakeAlertEvent alert){
        UserClient.UserInfo user = userClient.getUser(userId);

        if (user == null) {
            log.warn("Kullanıcı bulunamadı: {}", userId);
            return;
        }
        log.info("📬 Bildirim gönderiliyor: {} ({}) | FCM: {}",
                user.name(), user.city(),
                user.fcmToken() != null ? "var" : "YOK");



        String smsText = smsService.buildEarthquakeMessage(
                alert.location(),
                alert.magnitude(),
                alert.severity(),
                user.city()
        );

        smsService.sendSms(user.phoneNumber(), smsText);

        pushService.sendPushNotification(
                user.fcmToken(),
                "🔴 Deprem Uyarısı — " + alert.location(),
                String.format("M%.1f büyüklüğünde deprem. Güvende kalın!",
                        alert.magnitude())
        );
    }
}
