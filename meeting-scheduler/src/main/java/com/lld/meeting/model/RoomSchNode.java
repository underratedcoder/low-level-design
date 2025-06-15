package com.lld.meeting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomSchNode {
    private int start;
    private int end;
    private RoomSchNode prev;
    private RoomSchNode next;
}