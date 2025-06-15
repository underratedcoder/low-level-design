package com.lld.airline.enums;

public enum SeatStatus {
    FREE,       // Available for booking
    RESERVED,   // Temporarily held for a booking
    BOOKED,     // Confirmed and paid
    MAINTENANCE // Not available for booking due to maintenance
}
