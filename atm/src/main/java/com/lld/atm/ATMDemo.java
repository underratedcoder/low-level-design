package com.lld.atm;

import java.util.*;
import java.util.stream.Collectors;

public class ATMDemo {
    public static void main(String[] args) {
        ATMMachine atm = new ATMMachine();
        
        System.out.println("=== ATM System Demo ===\n");
        
        // Scenario 1: Successful withdrawal
        System.out.println("Scenario 1: Successful Withdrawal");
        atm.insertCard("1234567890");
        atm.enterPin("1234");
        atm.selectTransaction("WITHDRAWAL");
        atm.enterAmount(500.0);
        
        System.out.println("\n" + "=".repeat(40) + "\n");
        
        // Scenario 2: Balance inquiry
        System.out.println("Scenario 2: Balance Inquiry");
        atm.insertCard("0987654321");
        atm.enterPin("5678");
        atm.selectTransaction("BALANCE_INQUIRY");
        
        System.out.println("\n" + "=".repeat(40) + "\n");
        
        // Scenario 3: Failed PIN attempts
        System.out.println("Scenario 3: Failed PIN Attempts");
        atm.insertCard("1234567890");
        atm.enterPin("0000"); // Wrong PIN
        atm.enterPin("1111"); // Wrong PIN
        atm.enterPin("2222"); // Wrong PIN - This should block the card
        
        System.out.println("\n" + "=".repeat(40) + "\n");
        
        // Scenario 4: Deposit
        System.out.println("Scenario 4: Deposit Transaction");
        atm.insertCard("0987654321");
        atm.enterPin("5678");
        atm.selectTransaction("DEPOSIT");
        atm.enterAmount(200.0);
    }
}