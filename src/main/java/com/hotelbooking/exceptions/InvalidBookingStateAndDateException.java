package com.hotelbooking.exceptions;

public class InvalidBookingStateAndDateException extends RuntimeException{
    public InvalidBookingStateAndDateException(String message){
        super(message);
    }
}
