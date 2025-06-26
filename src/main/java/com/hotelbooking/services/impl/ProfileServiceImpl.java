package com.hotelbooking.services.impl;

import com.hotelbooking.dtos.ProfileDto;
import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.entities.Profile;
import com.hotelbooking.entities.User;
import com.hotelbooking.exceptions.NotFoundException;
import com.hotelbooking.repositories.ProfileRepository;
import com.hotelbooking.repositories.UserRepository;
import com.hotelbooking.services.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + File.separator + "profile-image";

    @Override
    public ResponseDto createProfile(Long userId, ProfileDto profileDto, MultipartFile imageFile) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Profile profileToSave = modelMapper.map(profileDto, Profile.class);
        profileToSave.setUser(user);

        if (profileDto.getAddress() != null)
            profileToSave.setAddress(profileDto.getAddress());

        if (profileDto.getCity() != null)
            profileToSave.setCity(profileDto.getCity());

        if (profileDto.getCountry() != null)
            profileToSave.setCountry(profileDto.getCountry());

        if (profileDto.getOccupation() != null)
            profileToSave.setOccupation(profileDto.getOccupation());

        if (profileDto.getGender() != null)
            profileToSave.setGender(profileDto.getGender());

        if(imageFile != null){
            String imagePath = saveImage(imageFile);
            profileToSave.setProfilePictureUrl(imagePath);
        }

        profileRepository.save(profileToSave);
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message("Profile Successfully Created!")
                .build();
    }

    @Override
    public ResponseDto updateProfile(Long profileId, ProfileDto profileDto, MultipartFile imageFile) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException("Profile not found with id: " + profileId));

        // Store old image path before potential update
        String oldImagePath = profile.getProfilePictureUrl();

        if (profileDto.getAddress() != null) {
            profile.setAddress(profileDto.getAddress());
        }
        if (profileDto.getCity() != null) {
            profile.setCity(profileDto.getCity());
        }
        if (profileDto.getCountry() != null) {
            profile.setCountry(profileDto.getCountry());
        }
        if (profileDto.getOccupation() != null) {
            profile.setOccupation(profileDto.getOccupation());
        }
        if (profileDto.getGender() != null) {
            profile.setGender(profileDto.getGender());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String newImagePath = saveImage(imageFile);
            profile.setProfilePictureUrl(newImagePath);
        }

        // Delete old image if it exists
        if (oldImagePath != null && !oldImagePath.isEmpty()) {
            deleteOldImage(oldImagePath);
        }

        profileRepository.save(profile);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Profile updated successfully")
                .build();
    }

    // Save Image Into IMAGE_DIRECTORY
    private String saveImage(MultipartFile imageFile){
        if(!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }

        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        try {
            File destinationFile = new File(directory, uniqueFileName);
            imageFile.transferTo(destinationFile);
            return "profile-image/" + uniqueFileName;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to save image: " + ex.getMessage());
        }
    }

    // Delete Image From IMAGE_DIRECTORY
    private void deleteOldImage(String oldImagePath) {
        try {
            // Extract filename from path
            String filename = oldImagePath.replace("profile-image/", "");
            File oldImageFile = new File(IMAGE_DIRECTORY, filename);

            if (oldImageFile.exists()) {
                if (!oldImageFile.delete()) {
                    log.warn("Failed to delete old image file: {}", oldImagePath);
                }
            }
        } catch (SecurityException e) {
            log.error("Security exception while deleting old image: {}", e.getMessage());
        }
    }
}
