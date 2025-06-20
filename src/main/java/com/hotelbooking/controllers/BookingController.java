package com.hotelbooking.controllers;

import com.hotelbooking.dtos.BookingDto;
import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> getAllBooking(){
        return ResponseEntity.ok(bookingService.getAllBooking());
    }

    @PostMapping()
    public ResponseEntity<ResponseDto> createBooking(@RequestBody BookingDto bookingDto){
        return ResponseEntity.ok(bookingService.createBooking(bookingDto));
    }

    @GetMapping("/{reference}")
    public ResponseEntity<ResponseDto> getBookingByRef(@PathVariable String reference){
        return ResponseEntity.ok(bookingService.findBookingByReferenceNo(reference));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> updateBooking(@RequestBody BookingDto bookingDto){
        return ResponseEntity.ok(bookingService.updateBooking(bookingDto));
    }
}
