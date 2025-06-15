// SettlementController.java
package com.lld.splitwise.controller;

import com.lld.splitwise.model.Member;
import com.lld.splitwise.model.SettleUp;
import com.lld.splitwise.service.BalanceSheetManager;
import com.lld.splitwise.service.TransactionManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SettlementController {
    private final BalanceSheetManager balanceSheetManager;
    private final TransactionManager transactionManager;
    
    public SettlementController() {
        this.balanceSheetManager = BalanceSheetManager.getInstance();
        this.transactionManager = TransactionManager.getInstance();
    }
    
    public Map<Member, Map<Member, Double>> getBalances(UUID groupId, UUID userId) {
        return balanceSheetManager.getBalances(groupId, userId);
    }
    
    public Map<Member, Map<Member, Double>> getSimplifiedBalances(UUID groupId) {
        return balanceSheetManager.getSimplifiedBalances(groupId);
    }
    
    public void settleDebt(UUID groupId, UUID payerId, UUID receiverId, double amount) {
        balanceSheetManager.settleDebt(groupId, payerId, receiverId, amount);
    }
    
    public List<SettleUp> getOptimalSettlementPlan(UUID groupId) {
        return transactionManager.getOptimalSettlementPlan(groupId);
    }
    
    public List<SettleUp> getGroupSettlements(UUID groupId) {
        return transactionManager.getGroupSettlements(groupId);
    }
}