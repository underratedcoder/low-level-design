package com.lld.sample.parking.dao;

import com.lld.sample.parking.model.Booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAO {
    private List<Booking> bookings = new ArrayList<>();

    // userId will be null when user directly go to parking without booking
    public String createBooking(String vehicleNo, Long userId, LocalDateTime entryTime, String parkingId, String spotId) {
        String bookingId = "BOOK" + (bookings.size() + 1);
        Booking booking = new Booking(bookingId, vehicleNo, userId, entryTime, parkingId, spotId);
        bookings.add(booking);
        return bookingId;
    }

    public Optional<Booking> getBookingDetails(String bookingId) {
        return bookings.stream()
                .filter(booking -> booking.getBookingId().equals(bookingId))
                .findFirst();
    }

    public Optional<Booking> getUserActiveBookings(Long userId) {
        return bookings.stream()
                .filter(booking -> booking.getUserId().equals(userId) && booking.getExitTime() == null)
                .findFirst();
    }

    public void updateBookingExitTime(String bookingId, LocalDateTime exitTime) {
        bookings.stream()
                .filter(booking -> booking.getBookingId().equals(bookingId))
                .findFirst()
                .ifPresent(booking -> booking.setExitTime(exitTime));
    }
}