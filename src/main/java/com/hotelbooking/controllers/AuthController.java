package com.hotelbooking.controllers;

import com.hotelbooking.dtos.LoginDto;
import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.dtos.SignupDto;
import com.hotelbooking.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody @Valid SignupDto request){
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginUser(@RequestBody @Valid LoginDto request,HttpServletResponse response){
        return ResponseEntity.ok(userService.loginUser(request,response));
    }
}
