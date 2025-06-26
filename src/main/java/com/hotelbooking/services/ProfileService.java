package com.hotelbooking.services;


import com.hotelbooking.dtos.ProfileDto;
import com.hotelbooking.dtos.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ResponseDto createProfile(Long userId, ProfileDto profileDto, MultipartFile imageFile);
    ResponseDto updateProfile(Long profileId,ProfileDto profileDto, MultipartFile imageFile);
}
