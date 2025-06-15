package com.lld.airline.model;

import lombok.Data;

@Data
public class Passenger {
    private Long passengerId;
    private Long userId;
    private String name;
    private int age;
}