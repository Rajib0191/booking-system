package com.hotelbooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotelbooking.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDto {
    private Long id;

    @NotBlank(message = "Subject is required!")
    private String subject;

    @NotBlank(message = "Recipient is required!")
    private String recipient;

    @Column(columnDefinition = "TEXT")
    private String body;
    private String bookingReference;

    private NotificationType type;

    private LocalDateTime createdAt;
}
