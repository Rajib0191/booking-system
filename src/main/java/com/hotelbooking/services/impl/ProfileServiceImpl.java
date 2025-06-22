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

//        if (profileRepository.existsByUser(user)) {
//            throw new IllegalArgumentException("Profile already exists for user id: " + userId);
//        }

        Profile profileToSave = modelMapper.map(profileDto, Profile.class);
        profileToSave.setUser(user);

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
    public ResponseDto updateProfile(ProfileDto profileDto, MultipartFile imageFile) {
        return null;
    }

    @Override
    public ResponseDto getProfileById(Long profileId) {
        return null;
    }

    @Override
    public void deleteProfile(Long profileId) {

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
}
