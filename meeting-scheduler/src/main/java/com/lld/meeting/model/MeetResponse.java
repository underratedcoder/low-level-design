package com.lld.meeting.model;

import com.lld.meeting.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetResponse {
    private String meetingId;
    private boolean success;
    private RoomStatus status;
}