
// BalanceRepository.java
package com.lld.splitwise.repository.impl;

import com.lld.splitwise.model.BalanceSheet;
import com.lld.splitwise.model.Group;
import com.lld.splitwise.model.SettleUp;
import com.lld.splitwise.repository.IBalanceRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BalanceRepository implements IBalanceRepository {
    private static BalanceRepository instance;
    private final Map<UUID, BalanceSheet> balanceSheets;
    private final Map<UUID, List<SettleUp>> settlements;

    private BalanceRepository() {
        this.balanceSheets = new HashMap<>();
        this.settlements = new HashMap<>();
    }


    public static synchronized BalanceRepository getInstance() {
        if (instance == null) {
            instance = new BalanceRepository();
        }
        return instance;
    }

    @Override
    public BalanceSheet saveBalanceSheet(BalanceSheet balanceSheet) {
        if (balanceSheet.getId() == null) {
            balanceSheet.setId(UUID.randomUUID());
        }
        balanceSheets.put(balanceSheet.getId(), balanceSheet);
        return balanceSheet;
    }

    @Override
    public BalanceSheet findBalanceSheetByGroup(Group group) {
        return balanceSheets.values().stream()
            .filter(sheet -> sheet.getGroup().getId().equals(group.getId()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public SettleUp saveSettlement(SettleUp settleUp) {
        if (settleUp.getId() == null) {
            settleUp.setId(UUID.randomUUID());
        }

        UUID groupId = settleUp.getGroup().getId();
        if (!settlements.containsKey(groupId)) {
            settlements.put(groupId, new ArrayList<>());
        }

        // Update balance sheet ?

        settlements.get(groupId).add(settleUp);
        return settleUp;
    }

    @Override
    public List<SettleUp> findSettlementsByGroup(Group group) {
        return settlements.getOrDefault(group.getId(), new ArrayList<>());
    }
}