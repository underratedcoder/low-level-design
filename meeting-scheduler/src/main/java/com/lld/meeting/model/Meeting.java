package com.lld.meeting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meeting {
    private String id;
    private int start;
    private int end;
    private int noOfPeople;
    private String roomNo;
    private LocalDate date;
}