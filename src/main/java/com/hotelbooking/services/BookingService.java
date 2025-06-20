package com.hotelbooking.services;

import com.hotelbooking.dtos.BookingDto;
import com.hotelbooking.dtos.ResponseDto;

public interface BookingService {
    ResponseDto getAllBooking();
    ResponseDto createBooking(BookingDto bookingDto);
    ResponseDto findBookingByReferenceNo(String bookingRef);
    ResponseDto updateBooking(BookingDto bookingDto);
}
