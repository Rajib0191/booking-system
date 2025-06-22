package com.hotelbooking.entities;

import com.hotelbooking.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Room Number is required!")
    @Min(value = 1, message = "Room Number must be at least 1")
    @Column(unique = true)
    private Integer roomNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Room type is required!")
    private RoomType roomType;

    @DecimalMin(value = "0.1", message = "Price per night is required!")
    private BigDecimal pricePerNight;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private String description; //additional data for room.
    private String imageUrl; //this will hold the room picture.

}
