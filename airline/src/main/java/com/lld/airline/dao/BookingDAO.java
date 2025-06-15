package com.lld.airline.dao;

import com.lld.airline.enums.BookingStatus;
import com.lld.airline.model.Booking;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class BookingDAO {
    private static volatile BookingDAO instance;

    public static BookingDAO getInstance() {
        if (instance == null) {
            synchronized (BookingDAO.class) {
                if (instance == null) {
                    instance = new BookingDAO();
                }
            }
        }
        return instance;
    }

    private final Map<Long, Booking> bookings = new ConcurrentHashMap<>();
    private static final AtomicLong bookingIdGenerator = new AtomicLong(1);

    /**
     * Saves a new booking to the repository.
     */
    public Booking save(Booking booking) {
        booking.setBookingId(generateBookingId());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedDatetime(LocalDateTime.now());

        bookings.put(booking.getBookingId(), booking);

        log.info("Booking created: {}", booking);
        return booking;
    }

    /**
     * 1. Confirm a booking and marks it as CONFIRMED
     * 2. Fail a booking and marks it as FAILED
     * 3. Cancels a booking and marks it as CANCELLED
     */
    public boolean update(Long bookingId, BookingStatus status) {
        Booking booking = bookings.get(bookingId);

        booking.setStatus(BookingStatus.CONFIRMED);
        bookings.put(bookingId, booking);

        log.info("Booking updated: {}", bookingId);

        return true;
    }

    /**
     * Finds a booking by its ID.
     */
    public Booking findById(Long bookingId) {
        return bookings.get(bookingId);
    }

    /**
     * Fetches all bookings for a specific user.
     */
    public List<Booking> findByUserId(Long userId) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getUserId().equals(userId)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    private Long generateBookingId() {
        return bookingIdGenerator.getAndIncrement();
    }
}
