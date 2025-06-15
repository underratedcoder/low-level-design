package com.lld.airline.service.notify;

public class SMSNotification implements Observer {
    public void update(String message) {
        System.out.println("SMS Notification: " + message);
    }
}