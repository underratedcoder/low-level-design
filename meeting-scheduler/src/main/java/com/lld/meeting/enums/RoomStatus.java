package com.lld.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomStatus {
    ALREADY_BOOKED,
    BOOKED,
    AVAILABLE
}