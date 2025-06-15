package com.lld.airline.service.notify;

public class EmailNotification implements Observer {
    public void update(String message) {
        System.out.println("Email Notification: " + message);
    }
}