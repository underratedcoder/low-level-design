package com.lld.meeting;

import com.lld.meeting.model.MeetResponse;
import com.lld.meeting.model.RoomAvailabilityCheck;
import com.lld.meeting.service.MeetingService;
import com.lld.meeting.service.RoomService;

import java.time.LocalDate;

public class MeetingSchedulerApp {
    public static void main(String[] args) throws Exception {
        RoomService roomService = new RoomService();
        MeetingService meetingService = new MeetingService(roomService);

        String r1 = roomService.addRoom(10);
        String r2 = roomService.addRoom(15);

        LocalDate today = LocalDate.now();

        RoomAvailabilityCheck rac = new RoomAvailabilityCheck(today, 10, 12);
        System.out.println("Available Rooms from 10 to 12:");
        roomService.fetchAvailableRooms(rac).forEach(room ->
                System.out.println("RoomNo: " + room.getRoomNo() + ", Capacity: " + room.getCapacity())
        );

        MeetResponse m1 = meetingService.create(10, 12, 5, r1, today);

        MeetResponse m2 = meetingService.create(11, 13, 4, r1, today);

        MeetResponse m3 = meetingService.create(11, 13, 4, r2, today);

        MeetResponse reschedule = meetingService.reschedule(m3.getMeetingId(), 10, 12, r2, today);

        System.out.println(meetingService.cancel(m1.getMeetingId()));

        System.out.println("Available Rooms after cancellation from 10 to 12:");
        roomService.fetchAvailableRooms(rac).forEach(room ->
                System.out.println("RoomNo: " + room.getRoomNo() + ", Capacity: " + room.getCapacity())
        );
    }
}