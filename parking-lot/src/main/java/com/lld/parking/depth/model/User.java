package com.lld.parking.depth.model;

public class User {
    private Long userId;
    private String name;
    private String mobileNo;

    // Constructor, Getters, and Setters
    public User(Long userId, String name, String mobileNo) {
        this.userId = userId;
        this.name = name;
        this.mobileNo = mobileNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}