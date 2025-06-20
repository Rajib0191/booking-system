package com.hotelbooking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotelbooking.enums.PaymentGateway;
import com.hotelbooking.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDto {
    private Long id;
    private BookingDto booking;

    private BigDecimal amount;
    private String transactionId;

    private PaymentGateway paymentMethod;

    private PaymentStatus status;

    private LocalDateTime paymentDate;
    private String bookingReference;
    private String failureReason;

    private String approvalLink;
}
