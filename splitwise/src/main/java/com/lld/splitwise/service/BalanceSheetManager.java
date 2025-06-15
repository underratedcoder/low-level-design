// BalanceSheetManager.java (Complete)
package com.lld.splitwise.service;

import com.lld.splitwise.model.BalanceSheet;
import com.lld.splitwise.model.Expense;
import com.lld.splitwise.model.Group;
import com.lld.splitwise.model.Member;
import com.lld.splitwise.util.observer.SettlementNotificationObserver;
import com.lld.splitwise.repository.impl.BalanceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.PriorityQueue;
import java.util.Comparator;

public class BalanceSheetManager {
    private final BalanceRepository balanceRepository;
    private final SettlementNotificationObserver notificationObserver;
    
    private BalanceSheetManager() {
        balanceRepository = BalanceRepository.getInstance();
        notificationObserver = SettlementNotificationObserver.getInstance();
    }
    
    public void updateBalanceSheet(Expense expense) {
        Group group = expense.getGroup();
        Member payer = expense.getPaidBy();
        Map<Member, Double> userContributions = expense.getUserContributions();
        
        // Get or create balance sheet for this group
        BalanceSheet balanceSheet = balanceRepository.findBalanceSheetByGroup(group);
        
        if (balanceSheet == null) {
            balanceSheet = BalanceSheet.builder()
                    .group(group)
                    .userBalances(new HashMap<>())
                    .build();
        }
        
        Map<Member, Map<Member, Double>> userBalances = balanceSheet.getUserBalances();
        
        // Update balances based on the expense
        for (Map.Entry<Member, Double> entry : userContributions.entrySet()) {
            Member participant = entry.getKey();
            Double amount = entry.getValue();
            
            if (participant.getId().equals(payer.getId())) {
                continue; // Skip the payer, as they don't owe themselves
            }
            
            // Initialize maps if they don't exist
            userBalances.computeIfAbsent(participant, k -> new HashMap<>());
            userBalances.get(participant).computeIfAbsent(payer, k -> 0.0);
            
            userBalances.computeIfAbsent(payer, k -> new HashMap<>());
            userBalances.get(payer).computeIfAbsent(participant, k -> 0.0);
            
            // Update how much participant owes to payer
            double currentOwedAmount = userBalances.get(participant).get(payer);
            userBalances.get(participant).put(payer, currentOwedAmount + amount);
            
            // Update how much payer is owed by participant (negative value in payer's record)
            double currentOweAmount = userBalances.get(payer).get(participant);
            userBalances.get(payer).put(participant, currentOweAmount - amount);
        }
        
        // Save updated balance sheet
        balanceRepository.saveBalanceSheet(balanceSheet);
    }
    
    public Map<Member, Map<Member, Double>> getBalances(UUID groupId, UUID userId) {
        BalanceSheet balanceSheet = getBalanceSheetForGroup(groupId);
        Map<Member, Map<Member, Double>> filteredBalances = new HashMap<>();
        
        // Filter balances for the specified user
        balanceSheet.getUserBalances().forEach((user, balances) -> {
            if (user.getId().equals(userId)) {
                filteredBalances.put(user, balances);
            }
        });
        
        return filteredBalances;
    }
    
    private BalanceSheet getBalanceSheetForGroup(UUID groupId) {
        // Get the group from GroupManager
        Group group = GroupManager.getInstance().getGroup(groupId);
        
        // Get the balance sheet for this group
        BalanceSheet balanceSheet = balanceRepository.findBalanceSheetByGroup(group);
        
        if (balanceSheet == null) {
            balanceSheet = BalanceSheet.builder()
                    .group(group)
                    .userBalances(new HashMap<>())
                    .build();
            balanceRepository.saveBalanceSheet(balanceSheet);
        }
        
        return balanceSheet;
    }
    
    public Map<Member, Map<Member, Double>> getSimplifiedBalances(UUID groupId) {
        return simplifyDebts(groupId);
    }
    
    // This method implements a greedy algorithm to minimize the number of transactions
    public Map<Member, Map<Member, Double>> simplifyDebts(UUID groupId) {
        BalanceSheet balanceSheet = getBalanceSheetForGroup(groupId);
        Map<Member, Map<Member, Double>> optimizedBalances = new HashMap<>();
        
        // Calculate net balance for each user
        Map<Member, Double> netBalances = calculateNetBalances(balanceSheet);
        
        // Separate users who owe money (debtors) and users who are owed money (creditors)
        PriorityQueue<Map.Entry<Member, Double>> debtors = new PriorityQueue<>(
            Comparator.comparingDouble(Map.Entry::getValue)
        );
        
        PriorityQueue<Map.Entry<Member, Double>> creditors = new PriorityQueue<>(
            (a, b) -> Double.compare(b.getValue(), a.getValue())
        );
        
        // Populate the queues
        for (Map.Entry<Member, Double> entry : netBalances.entrySet()) {
            if (entry.getValue() < -0.01) { // Debtor (negative balance means they owe money)
                debtors.add(entry);
            } else if (entry.getValue() > 0.01) { // Creditor (positive balance means they are owed money)
                creditors.add(entry);
            }
        }
        
        // Create optimized transactions
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<Member, Double> debtor = debtors.poll();
            Map.Entry<Member, Double> creditor = creditors.poll();
            
            Member debtorMember = debtor.getKey();
            Member creditorMember = creditor.getKey();
            
            double debtorAmount = Math.abs(debtor.getValue());
            double creditorAmount = creditor.getValue();
            
            double transferAmount = Math.min(debtorAmount, creditorAmount);
            
            // Add transaction to optimized balances
            optimizedBalances.computeIfAbsent(debtorMember, k -> new HashMap<>());
            optimizedBalances.get(debtorMember).put(creditorMember, transferAmount);
            
            // Update remaining balances
            if (debtorAmount - transferAmount > 0.01) {
                debtor.setValue(-(debtorAmount - transferAmount));
                debtors.add(debtor);
            }
            
            if (creditorAmount - transferAmount > 0.01) {
                creditor.setValue(creditorAmount - transferAmount);
                creditors.add(creditor);
            }
        }
        
        return optimizedBalances;
    }
    
    private Map<Member, Double> calculateNetBalances(BalanceSheet balanceSheet) {
        Map<Member, Double> netBalances = new HashMap<>();
        
        // Initialize all users with zero balance
        for (Member member : balanceSheet.getUserBalances().keySet()) {
            netBalances.put(member, 0.0);
        }
        
        // Calculate net balance for each user
        for (Map.Entry<Member, Map<Member, Double>> userEntry : balanceSheet.getUserBalances().entrySet()) {
            Member member = userEntry.getKey();
            Map<Member, Double> userBalances = userEntry.getValue();
            
            for (Map.Entry<Member, Double> balanceEntry : userBalances.entrySet()) {
                double amount = balanceEntry.getValue();
                
                // Positive amount means user is owed money
                // Negative amount means user owes money
                netBalances.put(member, netBalances.get(member) + amount);
            }
        }
        
        return netBalances;
    }
    
    public void settleDebt(UUID groupId, UUID payerId, UUID receiverId, double amount) {
        Group group = GroupManager.getInstance().getGroup(groupId);
        Member payer = UserService.getInstance().getUser(payerId);
        Member receiver = UserService.getInstance().getUser(receiverId);
        
        // Get the balance sheet
        BalanceSheet balanceSheet = balanceRepository.findBalanceSheetByGroup(group);
        if (balanceSheet == null) {
            throw new IllegalStateException("No balance sheet found for this group");
        }
        
        Map<Member, Map<Member, Double>> userBalances = balanceSheet.getUserBalances();
        
        // Update balances
        if (userBalances.containsKey(payer) && userBalances.get(payer).containsKey(receiver)) {
            double currentBalance = userBalances.get(payer).get(receiver);
            
            // Update payer's balance with receiver
            userBalances.get(payer).put(receiver, currentBalance + amount);
            
            // Update receiver's balance with payer
            if (userBalances.containsKey(receiver) && userBalances.get(receiver).containsKey(payer)) {
                double reverseBalance = userBalances.get(receiver).get(payer);
                userBalances.get(receiver).put(payer, reverseBalance - amount);
            }
        }
        
        // Save updated balance sheet
        balanceRepository.saveBalanceSheet(balanceSheet);
        
        // Create settlement record
        TransactionManager.getInstance().createSettlement(groupId, payerId, receiverId, amount);
        
        // Notify users
        notificationObserver.notify(payer, "You have settled " + amount + " with " + receiver.getName());
        notificationObserver.notify(receiver, payer.getName() + " has settled " + amount + " with you");
    }

    /**
     * Object Creation
     */

     private static BalanceSheetManager instance;

    public static synchronized BalanceSheetManager getInstance() {
        if (instance == null) {
            instance = new BalanceSheetManager();
        }
        return instance;
    }
}