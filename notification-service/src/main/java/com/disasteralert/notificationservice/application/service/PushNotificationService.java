package com.disasteralert.notificationservice.application.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushNotificationService {

    public void sendPushNotification(
            String fcmToken,
            String title,
            String body
    ){
        if (fcmToken == null || fcmToken.isBlank()) {
            log.debug("FCM token yok, push notification atlandı");
            return;
        }

        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(fcmToken)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.debug("FCM token yok, push notification atlandı");
            return;

        } catch (Exception e) {
            log.error("❌ Push notification gönderilemedi: {}",
                    e.getMessage());        }
    }
}
