package com.hotelbooking.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private Long id;

    @Size(max = 100)
    private String address;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String country;

    @Size(max = 50)
    private String occupation;

    @Size(max = 20)
    private String gender;
    private String profilePictureUrl;
}
