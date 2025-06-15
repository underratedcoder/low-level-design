// ExpenseNotificationObserver.java
package com.lld.splitwise.util.observer;

import com.lld.splitwise.model.Member;

public class ExpenseNotificationObserver implements NotificationObserver {
    private static ExpenseNotificationObserver instance;
    
    private ExpenseNotificationObserver() {}
    
    public static synchronized ExpenseNotificationObserver getInstance() {
        if (instance == null) {
            instance = new ExpenseNotificationObserver();
        }
        return instance;
    }
    
    @Override
    public void notify(Member member, String message) {
        System.out.println("Expense Notification to " + member.getName() + ": " + message);
        // In a real application, this would send an email or push notification
    }
}