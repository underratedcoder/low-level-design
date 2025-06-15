// SettlementNotificationObserver.java
package com.lld.splitwise.util.observer;

import com.lld.splitwise.model.Member;

public class SettlementNotificationObserver implements NotificationObserver {
    private static SettlementNotificationObserver instance;
    
    private SettlementNotificationObserver() {}
    
    public static synchronized SettlementNotificationObserver getInstance() {
        if (instance == null) {
            instance = new SettlementNotificationObserver();
        }
        return instance;
    }
    
    @Override
    public void notify(Member member, String message) {
        System.out.println("Settlement Notification to " + member.getName() + ": " + message);
        // In a real application, this would send an email or push notification
    }
}