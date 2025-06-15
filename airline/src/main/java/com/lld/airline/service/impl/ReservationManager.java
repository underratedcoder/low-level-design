package com.lld.airline.service.impl;

import com.lld.airline.dao.FlightSeatDAO;
import com.lld.airline.model.FlightSeatModel;
import com.lld.airline.model.SeatReservation;
import java.util.List;

public class ReservationManager {
    private static volatile ReservationManager instance;

    public static ReservationManager getInstance() {
        if (instance == null) {
            synchronized (ReservationManager.class) {
                if (instance == null) {
                    instance = new ReservationManager(FlightSeatDAO.getInstance());
                }
            }
        }
        return instance;
    }

    private final FlightSeatDAO flightSeatDAO;

    private ReservationManager(FlightSeatDAO flightSeatDAO) {
        this.flightSeatDAO = flightSeatDAO;
    }

    /**
     * Handles seat reservation if user selects the seat
     */
    public List<FlightSeatModel> reserveSeats(SeatReservation seatReservation) {
        try {
            return flightSeatDAO.reserveSeats(
                    seatReservation.getFlightId(),
                    seatReservation.getSeatIds(),
                    seatReservation.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Seat reservation process failed: " + e.getMessage());
        }
    }

    public void confirmSeats(SeatReservation seatReservation) throws Exception {
        flightSeatDAO.confirmSeats(
                seatReservation.getFlightId(),
                seatReservation.getSeatIds(),
                seatReservation.getUserId());
    }

    public void releaseSeats(SeatReservation seatReservation) throws RuntimeException {
        flightSeatDAO.releaseSeats(
                seatReservation.getFlightId(),
                seatReservation.getSeatIds(),
                seatReservation.getUserId());
    }

    public void cancelSeats(SeatReservation seatReservation) throws RuntimeException {
        flightSeatDAO.cancelSeats(
                seatReservation.getFlightId(),
                seatReservation.getSeatIds(),
                seatReservation.getUserId());
    }
}
