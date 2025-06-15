package com.lld.eventexpiry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.time.Instant;

@Getter
@AllArgsConstructor
@ToString
public class Event {
    private final String id;
    private final int eventId;
    private final Instant expiryTime;
    private final Object payload; // Generic payload, can be customized
}