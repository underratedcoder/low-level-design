package com.lld.meeting.service;

import com.lld.meeting.enums.RoomStatus;
import com.lld.meeting.model.MeetResponse;
import com.lld.meeting.model.Meeting;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MeetingService {
    private final AtomicInteger meetingIdGenerator = new AtomicInteger(1);

    private final Map<String, Meeting> meetings = new ConcurrentHashMap<>();
    private final RoomService roomService;

    public MeetingService(RoomService roomService) {
        this.roomService = roomService;
    }

    public MeetResponse create(int start, int end, int noOfPeople, String roomNo, LocalDate date) {
        String id = "M" + meetingIdGenerator.getAndIncrement();
        Meeting meeting = new Meeting(id, start, end, noOfPeople, roomNo, date);

        RoomStatus status = roomService.bookRoom(meeting);
        if (status == RoomStatus.BOOKED) {
            meetings.put(meeting.getId(), meeting);
            return new MeetResponse(meeting.getId(),true, RoomStatus.BOOKED);
        }
        return new MeetResponse(meeting.getId(), false, RoomStatus.ALREADY_BOOKED);
    }

    public MeetResponse reschedule(String id, int start, int end, String roomNo, LocalDate date) {
        Meeting existing = meetings.get(id);

        roomService.cancelBooking(existing);
        Meeting updated = new Meeting(id, start, end, existing.getNoOfPeople(), roomNo, date);
        RoomStatus status = roomService.bookRoom(updated);

        if (status == RoomStatus.BOOKED) {
            meetings.put(id, updated);
            return new MeetResponse(id, true, RoomStatus.BOOKED);
        } else {
            roomService.bookRoom(existing); // Rollback
            return new MeetResponse(id, false, RoomStatus.ALREADY_BOOKED);
        }
    }

    public MeetResponse cancel(String meetingId) {
        Meeting meeting = meetings.remove(meetingId);

        roomService.cancelBooking(meeting);
        return new MeetResponse(meetingId, true, RoomStatus.AVAILABLE);
    }
}