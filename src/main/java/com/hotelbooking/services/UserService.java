package com.hotelbooking.services;

import com.hotelbooking.dtos.LoginDto;
import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.dtos.SignupDto;
import com.hotelbooking.dtos.UserDto;
import com.hotelbooking.entities.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    ResponseDto registerUser(SignupDto signupDto);
    ResponseDto loginUser(LoginDto loginDto, HttpServletResponse response);
    ResponseDto getAllUsers();
    ResponseDto getOwnAccountDetails();
    ResponseDto updateOwnAccount(UserDto userDto);
    ResponseDto deleteOwnAccount();
    ResponseDto getMyBookingHistory();
    User getCurrentLoggedInUser();
}
