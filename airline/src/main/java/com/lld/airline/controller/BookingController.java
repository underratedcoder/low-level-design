package com.lld.airline.controller;

import com.lld.airline.model.Booking;
import com.lld.airline.service.impl.BookingManager;

public class BookingController {
    private final BookingManager bookingManager = BookingManager.getInstance();

    /**
     * User proceed to payment
     * */
    public Booking createBooking(Booking booking) {
        return bookingManager.createBooking(booking);
    }

    /**
     * Payment is successful, confirm booking
     * */
    public Booking confirmBooking(Long bookingId) {
        return bookingManager.confirmBooking(bookingId);
    }

    /**
     * System cancelled when
     * 1. Payment failed
     * 2. Payment page closed
     * */
    public void failBooking(Long bookingId) {
        bookingManager.failBooking(bookingId);
    }

    /**
     * User requested to cancel booking
     * */
    public void cancelBooking(Long bookingId) {
        bookingManager.cancelBooking(bookingId);
    }
}
