package com.lld.airline.dao;

import com.lld.airline.enums.SeatStatus;
import com.lld.airline.exception.SeatBookingException;
import com.lld.airline.model.FlightModel;
import com.lld.airline.model.FlightSeatModel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
public class FlightSeatDAO {
    private static volatile FlightSeatDAO instance;

    public static FlightSeatDAO getInstance() {
        if (instance == null) {
            synchronized (FlightSeatDAO.class) {
                if (instance == null) {
                    instance = new FlightSeatDAO();
                }
            }
        }
        return instance;
    }
    private static final AtomicLong flightIdGenerator = new AtomicLong(1);
    private static final AtomicLong seatIdGenerator = new AtomicLong(1);

    private final ConcurrentHashMap<Long, FlightModel> flightMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, FlightSeatModel> flightSeatMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, SeatLockWrapper> seatLocks = new ConcurrentHashMap<>();

    /**
     * Adds a seat to the repository
     */
    public void addFlight(FlightModel flightModel) {
        Long flightId = flightIdGenerator.getAndIncrement();
        flightMap.put(flightId, flightModel);
    }

    /**
     * Adds a seat to the repository
     */
    public void addSeat(FlightSeatModel seat) {
        Long seatId = seatIdGenerator.getAndIncrement();
        seat.setSeatStatus(SeatStatus.FREE);

        flightSeatMap.put(seat.getSeatId(), seat);
        seatLocks.putIfAbsent(seat.getSeatId(), new SeatLockWrapper());
    }

    /**
     * Gets a seat by ID
     */
    public FlightSeatModel getSeat(Long seatId) {
        return flightSeatMap.get(seatId);
    }

    /**
     * Gets all available seats for a flight
     */
    public List<FlightSeatModel> getAvailableSeats(Long flightId) {
        return flightSeatMap.values().stream()
                .filter(seat -> seat.getFlightId().equals(flightId) && seat.getSeatStatus() == SeatStatus.FREE)
                .collect(Collectors.toList());
    }

    /**
     * Reserves a specified list of seats for a flight.
     * @return List of successfully reserved seats
     * @throws SeatBookingException if any seat is unavailable
     */
    public List<FlightSeatModel> reserveSeats(Long flightId, List<Long> seatIds, Long userId) throws SeatBookingException {
        // seatAndFlightValidation();

        // Sort to avoid deadlock scenario
        List<Long> sortedSeatIds = new ArrayList<>(seatIds);
        sortedSeatIds.sort(Comparator.naturalOrder());

        List<SeatLockWrapper> acquiredLocks = new ArrayList<>();
        List<FlightSeatModel> reservedSeats = new ArrayList<>();

        try {
            // Acquire locks on requested seat IDs
            for (Long seatId : sortedSeatIds) {

                SeatLockWrapper seatLockWrapper = seatLocks.get(seatId);
                seatLockWrapper.lock(userId); // This Throws exception if unable to acquire lock

                acquiredLocks.add(seatLockWrapper);
            }

            for (Long seatId : sortedSeatIds) {
                FlightSeatModel seat = flightSeatMap.get(seatId);

                // Ensure Seat Is Free
                if (seat.getSeatStatus() != SeatStatus.FREE) {
                    throw new SeatBookingException("Seat " + seatId + " is already " + seat.getSeatStatus().toString().toLowerCase());
                }
            }

            // In real world, below code might be executed partially
            for (Long seatId : sortedSeatIds) {
                FlightSeatModel seat = flightSeatMap.get(seatId);
                seat.setSeatStatus(SeatStatus.RESERVED);
                reservedSeats.add(seat);
            }

            log.info("Seats reserved successfully: {}", sortedSeatIds);
            return reservedSeats;

        } catch (SeatBookingException e) {

            // Rollback any status changes if partial execution happened
            for (FlightSeatModel seat : reservedSeats) {
                seat.setSeatStatus(SeatStatus.FREE);
            }

            // Release all acquired locks
            for (SeatLockWrapper seatLockWrapper : acquiredLocks) {
                seatLockWrapper.unlock();
            }

            log.warn("Seat reservation failed: {}", e.getMessage());

            throw e;
        }
    }

    /**
     * 
     * */
    public void confirmSeats(Long flightId, List<Long> seatIds, Long userId) throws RuntimeException {
        seatIds.sort(Comparator.naturalOrder());

        List<SeatLockWrapper> alreadyLocked = new ArrayList<>();
        List<FlightSeatModel> alreadyReservedSeats = new ArrayList<>();

        try {
            // Check if lock by current user is intact on seat IDs
            for (Long seatId : seatIds) {
                SeatLockWrapper seatLockWrapper = seatLocks.get(seatId);

                if (!seatLockWrapper.isLocked() || !seatLockWrapper.getCurrentLockHolder().equals(userId)) {
                    throw new RuntimeException("Couldn't not book the tickets within time");
                }

                alreadyLocked.add(seatLockWrapper);
            }

            for (Long seatId : seatIds) {
                FlightSeatModel seat = flightSeatMap.get(seatId);
                seat.setSeatStatus(SeatStatus.BOOKED);
                alreadyReservedSeats.add(seat);
            }

            // Reduce no of seats in the flight
            FlightModel flight = flightMap.get(flightId);
            flight.setAvailableSeats(seatIds.size());

            log.info("Seats confirmed successfully: {}", seatIds);
        } catch (Exception ex) {
            // Rollback any status changes if partial execution happened

            for (FlightSeatModel seat : alreadyReservedSeats) {
                seat.setSeatStatus(SeatStatus.FREE);
            }

            throw new RuntimeException("Could not confirm the tickets");
        } finally {
            alreadyLocked.forEach(SeatLockWrapper::unlock);
        }
    }

    /**
     * When booking is not done within TTL or booking can not be done due to payment failures.
     * */
    public void releaseSeats(Long flightId, List<Long> seatIds, Long userId) throws RuntimeException {
        seatIds.sort(Comparator.naturalOrder());

        List<SeatLockWrapper> alreadyLocked = new ArrayList<>();
        List<FlightSeatModel> alreadyReservedSeats = new ArrayList<>();

        try {
            // Check if lock by current user is intact on seat IDs
            for (Long seatId : seatIds) {
                SeatLockWrapper seatLockWrapper = seatLocks.get(seatId);
                if (seatLockWrapper.isLocked() && seatLockWrapper.getCurrentLockHolder().equals(userId)) {
                    alreadyLocked.add(seatLockWrapper);
                } else {
                    log.warn("Seat is not locked by current user");
                }
            }

            if (alreadyLocked.isEmpty()) {
                return;
            }

            for (Long seatId : seatIds) {
                FlightSeatModel seat = flightSeatMap.get(seatId);
                seat.setSeatStatus(SeatStatus.FREE);
                alreadyReservedSeats.add(seat);
            }

            alreadyLocked.forEach(SeatLockWrapper::unlock);

            log.info("Seats released successfully: {}", seatIds);
        } catch (Exception ex) {
            // Rollback any status changes if partial execution happened
            throw new RuntimeException("Could not release the seats");
        }
    }

    /**
     * Releases seats when a booking is canceled
     *
     * Why Locking Here ?
     * Race condition risk: If another thread is reserving the same seat while you're releasing it, you might end up with an inconsistent state.
     *
     */
    public void cancelSeats(Long flightId, List<Long> seatIds, Long userId) {
        for (Long seatId : seatIds) {
            FlightSeatModel seat = flightSeatMap.get(seatId);
            seat.setSeatStatus(SeatStatus.FREE);
        }
        log.info("Seat released successfully");
    }

}
