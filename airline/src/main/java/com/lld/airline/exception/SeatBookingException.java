package com.lld.airline.exception;

public class SeatBookingException extends RuntimeException {
    public SeatBookingException(String message) {
        super(message);
    }
}