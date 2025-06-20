package com.hotelbooking.services.impl;

import com.hotelbooking.dtos.NotificationDto;
import com.hotelbooking.enums.NotificationType;
import com.hotelbooking.notification.Notification;
import com.hotelbooking.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationImplement implements Notification {
    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;

    @Override
    @Async
    public void sendEmail(NotificationDto notificationDto) {
        log.info("Inside Email Send");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notificationDto.getRecipient());
        simpleMailMessage.setSubject(notificationDto.getSubject());
        simpleMailMessage.setText(notificationDto.getBody());

        javaMailSender.send(simpleMailMessage);

        //Save to Database
        com.hotelbooking.entities.Notification notification = com.hotelbooking.entities.Notification.builder()
                .recipient(notificationDto.getRecipient())
                .body(notificationDto.getBody())
                .subject(notificationDto.getSubject())
                .bookingReference(notificationDto.getBookingReference())
                .notificationType(NotificationType.EMAIL)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendSms() {

    }

    @Override
    public void sendWhatsApp() {

    }
}
