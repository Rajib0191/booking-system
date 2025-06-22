package com.hotelbooking.controllers;

import com.hotelbooking.dtos.ProfileDto;
import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.services.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseDto> createProfile(
            @PathVariable Long userId,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String country,
            @RequestParam String bio,
            @RequestParam String gender,
            @RequestParam MultipartFile profilePictureUrl) {

        ProfileDto dto = ProfileDto.builder()
                .address(address)
                .gender(gender)
                .city(city)
                .country(country)
                .bio(bio)
                .build();

        return ResponseEntity.ok(profileService.createProfile(userId,dto,profilePictureUrl));
    }
}
