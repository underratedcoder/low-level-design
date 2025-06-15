package com.lld.airline.controller;

import com.lld.airline.model.FlightSeatModel;
import com.lld.airline.model.SeatReservation;
import com.lld.airline.service.impl.ReservationManager;

import java.util.List;

public class ReservationController {
    private final ReservationManager reservationManager = ReservationManager.getInstance();

    /**
    * User select seats from the UI
    */
    public List<FlightSeatModel> reserveSeats(SeatReservation seatReservation) {
        return reservationManager.reserveSeats(seatReservation);
    }

    /**
     * System cancel seats after TTL
     */
    public void releaseSeats(SeatReservation seatReservation) {
        reservationManager.releaseSeats(seatReservation);
    }
}
