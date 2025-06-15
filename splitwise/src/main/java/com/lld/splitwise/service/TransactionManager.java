// TransactionManager.java
package com.lld.splitwise.service;

import com.lld.splitwise.model.Group;
import com.lld.splitwise.model.Member;
import com.lld.splitwise.model.SettleUp;
import com.lld.splitwise.model.Transaction;
import com.lld.splitwise.repository.impl.BalanceRepository;
import com.lld.splitwise.repository.impl.TransactionRepository;

import java.util.*;

public class TransactionManager {
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final GroupManager groupManager;
    private final UserService userService;
    
    private TransactionManager() {
        transactionRepository = TransactionRepository.getInstance();
        balanceRepository = BalanceRepository.getInstance();
        groupManager = GroupManager.getInstance();
        userService = UserService.getInstance();
    }
    
    public Transaction createTransaction(UUID fromUserId, UUID toUserId, double amount, String description) {
        Member fromMember = userService.getUser(fromUserId);
        Member toMember = userService.getUser(toUserId);
        
        Transaction transaction = Transaction.builder()
                .from(fromMember)
                .to(toMember)
                .amount(amount)
                .description(description)
                .transactionDate(new Date())
                .build();
        
        return transactionRepository.save(transaction);
    }
    
    public SettleUp createSettlement(UUID groupId, UUID payerId, UUID receiverId, double amount) {
        Group group = groupManager.getGroup(groupId);
        Member payer = userService.getUser(payerId);
        Member receiver = userService.getUser(receiverId);
        
        // Create a transaction for this settlement
        createTransaction(payerId, receiverId, amount, "Settlement in group: " + group.getName());
        
        // Create a settlement record
        SettleUp settleUp = SettleUp.builder()
                .payer(payer)
                .receiver(receiver)
                .amount(amount)
                .settlementDate(new Date())
                .group(group)
                .build();
        
        return balanceRepository.saveSettlement(settleUp);
    }
    
    public List<Transaction> getUserTransactions(UUID userId) {
        return transactionRepository.findByUser(userService.getUser(userId));
    }
    
    public List<SettleUp> getGroupSettlements(UUID groupId) {
        Group group = groupManager.getGroup(groupId);
        return balanceRepository.findSettlementsByGroup(group);
    }
    
    // Get the optimal settlement plan for a group
    public List<SettleUp> getOptimalSettlementPlan(UUID groupId) {
        // This uses the simplified balances from BalanceSheetManager
        BalanceSheetManager balanceSheetManager = BalanceSheetManager.getInstance();
        Group group = groupManager.getGroup(groupId);
        
        // Get the optimized balances
        Map<Member, Map<Member, Double>> optimizedBalances = balanceSheetManager.getSimplifiedBalances(groupId);
        List<SettleUp> settlements = new ArrayList<>();
        
        // Convert optimized balances to settlements
        for (Map.Entry<Member, Map<Member, Double>> userEntry : optimizedBalances.entrySet()) {
            Member payer = userEntry.getKey();
            
            for (Map.Entry<Member, Double> balanceEntry : userEntry.getValue().entrySet()) {
                Member receiver = balanceEntry.getKey();
                double amount = balanceEntry.getValue();
                
                if (amount > 0.01) {
                    SettleUp settleUp = SettleUp.builder()
                            .payer(payer)
                            .receiver(receiver)
                            .amount(amount)
                            .group(group)
                            .build();
                    
                    settlements.add(settleUp);
                }
            }
        }
        
        return settlements;
    }

    /**
     * Object Creation
     */

    private static TransactionManager instance;

    public static synchronized TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }
}