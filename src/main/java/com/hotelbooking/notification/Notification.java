package com.hotelbooking.notification;

import com.hotelbooking.dtos.NotificationDto;

public interface Notification {
    void sendEmail(NotificationDto notificationDto);
    void sendSms();
    void sendWhatsApp();
}
