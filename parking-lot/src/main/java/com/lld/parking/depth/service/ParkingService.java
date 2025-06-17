package com.lld.parking.depth.service;

import com.lld.parking.depth.dao.BookingDAO;
import com.lld.parking.depth.dao.FareDAO;
import com.lld.parking.depth.dao.SpotDAO;
import com.lld.parking.depth.model.Booking;
import com.lld.parking.depth.model.ExitParking;
import com.lld.parking.depth.model.Spot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ParkingService {
    private SpotDAO spotDAO = new SpotDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private FareDAO fareDAO = new FareDAO();
    private FareCalculator fareCalculator = new FareCalculator();

    public List<Spot> fetchAvailableSpots(String parkingId, Integer floorNo, LocalDateTime entryTime) {
        List<Spot> spots = spotDAO.getParkingSpots(parkingId, floorNo);

        List<String> spotIds = spots.stream()
                .map(Spot::getSpotId)
                .collect(Collectors.toList());

        // TODO - Call BookingDAO to check which all spots are available - START
        Set<String> availableSpotIds = spots.stream()
                .map(Spot::getSpotId)
                .collect(Collectors.toSet());
        // TODO - Call BookingDAO to check which all spots are available - END

        return spots.stream()
                .filter(s -> availableSpotIds.contains(s.getSpotId()))
                .collect(Collectors.toList());
    }

    public String bookSpot(String vehicleNo, Long userId, LocalDateTime entryTime, String parkingId, String spotId) {
        return bookingDAO.createBooking(vehicleNo, userId, entryTime, parkingId, spotId);
    }

    public ExitParking exitParking(String bookingId) {
        Optional<Booking> bookingOpt = bookingDAO.getBookingDetails(bookingId);

        if (bookingOpt.isPresent()) {

            Booking booking = bookingOpt.get();
            booking.setExitTime(LocalDateTime.now());

            bookingDAO.updateBookingExitTime(bookingId, booking.getExitTime());

            double fare = fareCalculator.calculateFare(booking.getEntryTime(), booking.getExitTime(), booking.getVehicleType());

            fareDAO.createFare(bookingId, fare);

            return ExitParking.builder()
                    .bookingId(bookingId)
                    .fare(fare)
                    .entryTime(booking.getEntryTime())
                    .exitTime(booking.getExitTime())
                    .build();
        }

        throw new RuntimeException("Booking not found");
    }
}