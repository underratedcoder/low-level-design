package com.lld.airline.service.impl;

import com.lld.airline.enums.BookingStatus;
import com.lld.airline.model.*;
import com.lld.airline.dao.BookingDAO;
import com.lld.airline.service.notify.NotificationService;

import java.time.LocalDateTime;

public class BookingManager {
    private static volatile BookingManager instance;

    public static BookingManager getInstance() {
        if (instance == null) {
            synchronized (BookingManager.class) {
                if (instance == null) {
                    instance = new BookingManager(
                            BookingDAO.getInstance(),
                            ReservationManager.getInstance(),
                            NotificationService.getInstance()
                    );
                }
            }
        }
        return instance;
    }

    private final BookingDAO bookingDAO;
    private final ReservationManager reservationManager;
    private final NotificationService notificationService;

    private BookingManager(
            BookingDAO bookingDAO,
            ReservationManager reservationManager,
            NotificationService notificationService
    ) {
            this.bookingDAO = bookingDAO;
        this.reservationManager = reservationManager;
            this.notificationService = notificationService;
    }

    /**
     *
     */
    public Booking createBooking(Booking booking) {
        try {
            // Create a Booking Entry
            booking.setCreatedDatetime(LocalDateTime.now());
            booking.setStatus(BookingStatus.PENDING);
            return bookingDAO.save(booking);
        } catch (Exception e) {
            throw new RuntimeException("Booking creation failed: " + e.getMessage());
        }
    }

    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingDAO.findById(bookingId);

        if (booking == null || booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Cannot confirm an invalid or already confirmed booking");
        }

        SeatReservation seatReservation = SeatReservation.builder()
                .flightId(booking.getFlightId())
                .userId(booking.getUserId())
                .noOfSeats(booking.getNoOfTravelers())
                .seatIds(booking.getSeatIds())
                .build();

        try {
            // Step 3: Confirm Seats
            reservationManager.confirmSeats(seatReservation);

            // Step 4: Confirm Booking
            bookingDAO.update(booking.getBookingId(), BookingStatus.CONFIRMED);

            // Step 5: Send Notification
            notificationService.notifyAllObservers("Booking confirmed: " + booking.getBookingId());

            return booking;
        } catch (Exception e) {
            reservationManager.releaseSeats(seatReservation);
            throw new RuntimeException("Booking process failed: " + e.getMessage());
        }
    }

    public void failBooking(Long bookingId) {
        try {
            Booking booking = bookingDAO.findById(bookingId);

            if (booking == null || booking.getStatus() != BookingStatus.PENDING) {
                throw new RuntimeException("Cannot cancel an invalid or already cancelled booking");
            }

            bookingDAO.update(booking.getBookingId(), BookingStatus.FAILED);

            SeatReservation seatReservation = SeatReservation.builder()
                    .flightId(booking.getFlightId())
                    .userId(booking.getUserId())
                    .noOfSeats(booking.getNoOfTravelers())
                    .seatIds(booking.getSeatIds())
                    .build();

            reservationManager.cancelSeats(seatReservation);

            // Notify user about cancellation and refund
            notificationService.notifyAllObservers("Booking failed for " + bookingId);
        } catch (Exception e) {
            // Mark booking CONFIRMED again as was not able to cancel it
            throw new RuntimeException("Failed to cancel booking: " + e.getMessage());
        }
    }

    public void cancelBooking(Long bookingId) {
        try {
            Booking booking = bookingDAO.findById(bookingId);

            if (booking == null || booking.getStatus() != BookingStatus.CONFIRMED) {
                throw new RuntimeException("Cannot cancel an invalid or already cancelled booking");
            }

            bookingDAO.update(booking.getBookingId(), BookingStatus.CANCELLED);
            // This will trigger refund service

            SeatReservation seatReservation = SeatReservation.builder()
                    .flightId(booking.getFlightId())
                    .userId(booking.getUserId())
                    .noOfSeats(booking.getNoOfTravelers())
                    .seatIds(booking.getSeatIds())
                    .build();

            reservationManager.cancelSeats(seatReservation);

            // Notify user about cancellation and refund
            notificationService.notifyAllObservers("Booking cancelled for " + bookingId);
        } catch (Exception e) {
            // Mark booking CONFIRMED again as was not able to cancel it
            throw new RuntimeException("Failed to cancel booking: " + e.getMessage());
        }
    }
}
