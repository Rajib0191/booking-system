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
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) MultipartFile profilePictureUrl) {

        ProfileDto dto = ProfileDto.builder()
                .address(address)
                .gender(gender)
                .city(city)
                .country(country)
                .occupation(occupation)
                .build();

        return ResponseEntity.ok(profileService.createProfile(userId,dto,profilePictureUrl));
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ResponseDto> updateProfile(
            @PathVariable Long profileId,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) MultipartFile profilePictureUrl
    ) {
        ProfileDto dto = ProfileDto.builder()
                .address(address)
                .gender(gender)
                .city(city)
                .country(country)
                .occupation(occupation)
                .build();
log.info(String.valueOf(profilePictureUrl));
        return ResponseEntity.ok(profileService.updateProfile(profileId, dto, profilePictureUrl));
    }
}
