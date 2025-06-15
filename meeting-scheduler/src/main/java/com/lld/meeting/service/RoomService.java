package com.lld.meeting.service;

import com.lld.meeting.enums.RoomStatus;
import com.lld.meeting.model.Meeting;
import com.lld.meeting.model.Room;
import com.lld.meeting.model.RoomAvailabilityCheck;
import com.lld.meeting.model.RoomSchNode;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomService {
    private final AtomicInteger roomIdGenerator = new AtomicInteger(100);

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<LocalDate, Map<String, RoomSchNode>> roomBooking = new ConcurrentHashMap<>();

    public String addRoom(int capacity) {
        String roomNo = "R" + roomIdGenerator.getAndIncrement();
        Room room = new Room(roomNo, capacity);
        rooms.put(roomNo, room);
        return roomNo;
    }

    public synchronized RoomStatus bookRoom(Meeting meeting) {
        Map<String, RoomSchNode> roomMap = roomBooking.computeIfAbsent(meeting.getDate(), d -> new ConcurrentHashMap<>());

        roomMap.putIfAbsent(meeting.getRoomNo(), new RoomSchNode(-1, -1, null, null));
        RoomSchNode head = roomMap.get(meeting.getRoomNo());

        synchronized (head) {
            RoomSchNode curr = head.getNext();
            RoomSchNode prev = head;

            while (curr != null && curr.getStart() < meeting.getEnd()) {
                if (curr.getEnd() > meeting.getStart()) {
                    return RoomStatus.ALREADY_BOOKED;
                }
                prev = curr;
                curr = curr.getNext();
            }

            RoomSchNode newNode = new RoomSchNode(meeting.getStart(), meeting.getEnd(), prev, curr);
            prev.setNext(newNode);
            if (curr != null) {
                curr.setPrev(newNode);
            }

            return RoomStatus.BOOKED;
        }
    }

    public List<Room> fetchAvailableRooms(RoomAvailabilityCheck rac) {
        List<Room> available = new ArrayList<>();

        for (Room room : rooms.values()) {
            RoomSchNode head = roomBooking.getOrDefault(rac.getDate(), new HashMap<>())
                    .getOrDefault(room.getRoomNo(), new RoomSchNode(-1, -1, null, null));

            boolean isAvailable = true;
            RoomSchNode curr = head.getNext();
            while (curr != null) {
                if (curr.getStart() < rac.getEnd() && curr.getEnd() > rac.getStart()) {
                    isAvailable = false;
                    break;
                }
                curr = curr.getNext();
            }
            if (isAvailable) {
                available.add(room);
            }
        }

        return available;
    }

    public void cancelBooking(Meeting meeting) {
        Map<String, RoomSchNode> roomMap = roomBooking.get(meeting.getDate());
        if (roomMap == null || !roomMap.containsKey(meeting.getRoomNo())) return;

        RoomSchNode head = roomMap.get(meeting.getRoomNo());
        synchronized (head) {
            RoomSchNode curr = head.getNext();
            while (curr != null) {
                if (curr.getStart() == meeting.getStart() && curr.getEnd() == meeting.getEnd()) {
                    if (curr.getPrev() != null) curr.getPrev().setNext(curr.getNext());
                    if (curr.getNext() != null) curr.getNext().setPrev(curr.getPrev());
                    break;
                }
                curr = curr.getNext();
            }
        }
    }
}