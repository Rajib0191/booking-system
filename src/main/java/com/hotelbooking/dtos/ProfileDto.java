package com.hotelbooking.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    @Size(max = 100)
    private String address;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String country;

    @Size(max = 2000)
    private String bio;

    @Size(max = 20)
    private String gender;
    private String profilePictureUrl;
}
