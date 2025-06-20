package com.hotelbooking.services.impl;

import com.hotelbooking.dtos.*;
import com.hotelbooking.entities.Booking;
import com.hotelbooking.entities.User;
import com.hotelbooking.enums.UserRole;
import com.hotelbooking.exceptions.InvalidCredentialException;
import com.hotelbooking.exceptions.NotFoundException;
import com.hotelbooking.repositories.BookingRepository;
import com.hotelbooking.repositories.UserRepository;
import com.hotelbooking.security.JwtUtils;
import com.hotelbooking.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;

    @Override
    public ResponseDto registerUser(SignupDto signupDto) {
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        // Check for duplicate phone number
        if (userRepository.existsByPhoneNumber(signupDto.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists!");
        }

        UserRole role = UserRole.CUSTOMER;
        if(signupDto.getRole() != null){
            role = signupDto.getRole();
        }

        User userToSave = User.builder()
                .firstName(signupDto.getFirstName())
                .lastName(signupDto.getLastName())
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .phoneNumber(signupDto.getPhoneNumber())
                .role(role)
                .isActive(Boolean.TRUE)
                .build();

        userRepository.save(userToSave);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("User created successfully")
                .build();
    }

    @Override
    public ResponseDto loginUser(LoginDto loginDto, HttpServletResponse response) {
       User user = userRepository.findByEmail(loginDto.getEmail())
               .orElseThrow(() -> new NotFoundException("Email Not Found!"));

       if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
           throw new InvalidCredentialException("Password doesn't match!");
       }

       String token = jwtUtils.generateToken(user.getEmail());

        // Create secure HTTP-only cookie
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(false) // Enable in production (HTTPS only)
                .path("/")
                .maxAge(6 * 30 * 24 * 60 * 60) // 6 months in seconds
                .sameSite("Lax") // Helps prevent CSRF
//                .domain("http://localhost:3000") // Set your actual domain
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Login Successfully!")
                .role(user.getRole())
                .token(token)
                .isActive(user.getIsActive())
                .expirationTime("6 Month")
                .build();
    }

    @Override
    public ResponseDto getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDto> userDtoList = modelMapper.map(users, new TypeToken<List<UserDto>>(){}.getType());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .users(userDtoList)
                .build();
    }

    @Override
    public ResponseDto getOwnAccountDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found!"));

        log.info("Inside User Account Details");

        UserDto userDto = modelMapper.map(user,UserDto.class);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .user(userDto)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found!"));
    }

    @Override
    public ResponseDto updateOwnAccount(UserDto userDto) {
        User existingUser = getCurrentLoggedInUser();
        log.info("Inside Update User");
        if(userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
        if(userDto.getFirstName() != null) existingUser.setFirstName(userDto.getFirstName());
        if(userDto.getLastName() != null) existingUser.setLastName(userDto.getLastName());
        if(userDto.getPhoneNumber() != null) existingUser.setPhoneNumber(userDto.getPhoneNumber());
        if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(existingUser);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("User Updated Successfully!")
                .build();
    }

    @Override
    public ResponseDto deleteOwnAccount() {
        User existingUser = getCurrentLoggedInUser();
        userRepository.delete(existingUser);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("User Deleted Successfully!")
                .build();
    }

    @Override
    public ResponseDto getMyBookingHistory() {
        User existingUser = getCurrentLoggedInUser();
        List<Booking> bookingList = bookingRepository.findByUserId(existingUser.getId());
        List<BookingDto> bookingDtoList = modelMapper.map(bookingList, new TypeToken<List<BookingDto>>(){}.getType());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .bookings(bookingDtoList)
                .build();
    }


}
