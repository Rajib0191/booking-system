package com.hotelbooking.Utils;

import com.hotelbooking.entities.BookingReference;
import com.hotelbooking.repositories.BookingReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingRefGenerator {
    public final BookingReferenceRepository bookingReferenceRepository;

    public String generateBookingRef(){
        String bookingRef;
        do {
           bookingRef = generateRandomAlpNumericCode(10);
        }while (isBookingRefExist(bookingRef));

        saveBookingRefToDb(bookingRef);
        return bookingRef;
    }

    private String generateRandomAlpNumericCode(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i<length; i++){
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
    }

    private boolean isBookingRefExist(String bookingRef){
        return bookingReferenceRepository.findByReferenceNo(bookingRef).isPresent();
    }

    private void saveBookingRefToDb(String bookingRef){
        BookingReference bookingReference = BookingReference.builder()
                .referenceNo(bookingRef)
                .build();

        bookingReferenceRepository.save(bookingReference);
    }
}
