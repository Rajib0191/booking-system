package com.hotelbooking.services.impl;

import com.hotelbooking.Utils.BookingRefGenerator;
import com.hotelbooking.dtos.BookingDto;
import com.hotelbooking.dtos.NotificationDto;
import com.hotelbooking.dtos.ResponseDto;
import com.hotelbooking.entities.Booking;
import com.hotelbooking.entities.Room;
import com.hotelbooking.entities.User;
import com.hotelbooking.enums.BookingStatus;
import com.hotelbooking.enums.PaymentStatus;
import com.hotelbooking.exceptions.InvalidBookingStateAndDateException;
import com.hotelbooking.exceptions.NotFoundException;
import com.hotelbooking.notification.Notification;
import com.hotelbooking.repositories.BookingRepository;
import com.hotelbooking.repositories.RoomRepository;
import com.hotelbooking.services.BookingService;
import com.hotelbooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final Notification notification;
    private final BookingRefGenerator bookingRefGenerator;
    private final ModelMapper modelMapper;

    @Override
    public ResponseDto getAllBooking() {
        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BookingDto> bookingDtoList = modelMapper.map(bookingList, new TypeToken<List<BookingDto>>() {}.getType());
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .bookings(bookingDtoList)
                .build();
    }

    @Override
    public ResponseDto createBooking(BookingDto bookingDto) {
        User user = userService.getCurrentLoggedInUser();
        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(()-> new NotFoundException("Room Not Found"));

        //Validate Check-in date is not before today
        if(bookingDto.getCheckInDate().isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDateException("check in date cannot be before today");
        }

        //Validate Check-out date is not before check-in date
        if(bookingDto.getCheckOutDate().isBefore(bookingDto.getCheckInDate())){
            throw new InvalidBookingStateAndDateException("check out date cannot be before check in date");
        }

        //Validate Check-in date is not same check-out date
        if(bookingDto.getCheckInDate().isEqual(bookingDto.getCheckOutDate())){
            throw new InvalidBookingStateAndDateException("check in date cannot be equal check out date");
        }

        //Validate room availability
        boolean isRoomAvailable = bookingRepository.isRoomAvailable(room.getId(), bookingDto.getCheckInDate(), bookingDto.getCheckOutDate());
        if(!isRoomAvailable){
            throw new InvalidBookingStateAndDateException("Room is not available for the selected date range");
        }

        //calculate the total price needed to pay for the stay
        BigDecimal totalPrice = calculateTotalPrice(room,bookingDto);
        String bookingReference = bookingRefGenerator.generateBookingRef();

        //Create And Save Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        //generate the payment url which will be sent via mail
        String paymentUrl = "http://localhost:3000/payment/"+bookingReference+"/"+totalPrice;
        log.info("PAYMENT LINK: {}", paymentUrl);

        //send notification via email
        NotificationDto notificationDto = NotificationDto.builder()
                .recipient(user.getEmail())
                .subject("Booking Confirmation")
//                .body(String.format("""
//                        Your booking has been created successfully.
//                        Please proceed with your payment using the payment link below\s
//                        n%s""", paymentUrl))
                .body(String.format("Your booking has been created successfully.\n\n Please proceed with your payment using the payment link below "+"\n\n", paymentUrl))
                .bookingReference(bookingReference)
                .build();
        notification.sendEmail(notificationDto); //sending email

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully Booked")
                .booking(bookingDto)
                .build();
    }

    @Override
    public ResponseDto findBookingByReferenceNo(String bookingRef) {
        Booking booking = bookingRepository.findByBookingReference(bookingRef).
                orElseThrow(()-> new NotFoundException("Booking with reference no: "+ bookingRef + " Not Found"));
        log.info("Room info: {}", booking.getRoom());

        BookingDto bookingDto = modelMapper.map(booking,BookingDto.class);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .booking(bookingDto)
                .build();
    }

    @Override
    public ResponseDto updateBooking(BookingDto bookingDto) {
        if(bookingDto.getId() == null) throw new NotFoundException("Booking Id is required");

        Booking existingBooking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(()-> new NotFoundException("Booking Not Found"));

        if(bookingDto.getBookingStatus() != null){
            existingBooking.setBookingStatus(bookingDto.getBookingStatus());
        }
        if(bookingDto.getPaymentStatus() != null){
            existingBooking.setPaymentStatus(bookingDto.getPaymentStatus());
        }

        bookingRepository.save(existingBooking);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Booking Updated Successfully!")
                .build();
    }

    private BigDecimal calculateTotalPrice(Room room, BookingDto bookingDto){
        BigDecimal pricePerNight = room.getPricePerNight();
        long days = ChronoUnit.DAYS.between(bookingDto.getCheckInDate(), bookingDto.getCheckOutDate());
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }
}
