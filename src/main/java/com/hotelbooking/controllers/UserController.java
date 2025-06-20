package com.hotelbooking.controllers;

import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.dtos.UserDto;
import com.hotelbooking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateOwnAccount(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.updateOwnAccount(userDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteOwnAccount(){
        return ResponseEntity.ok(userService.deleteOwnAccount());
    }

    @GetMapping("/account")
    public ResponseEntity<ResponseDto> getOwnAccountDetails(){
        return ResponseEntity.ok(userService.getOwnAccountDetails());
    }

    @GetMapping("/booking")
    public ResponseEntity<ResponseDto> getBookingHistory(){
        return ResponseEntity.ok(userService.getMyBookingHistory());
    }
}
