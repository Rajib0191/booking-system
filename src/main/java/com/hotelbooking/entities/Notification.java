package com.hotelbooking.entities;

import com.hotelbooking.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @NotBlank(message = "Recipient is required!")
    private String recipient;

    private String body;
    private String bookingReference;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private final LocalDateTime createdAt=LocalDateTime.now();
}
