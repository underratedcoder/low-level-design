// IBalanceRepository.java
package com.lld.splitwise.repository;

import com.lld.splitwise.model.BalanceSheet;
import com.lld.splitwise.model.Group;
import com.lld.splitwise.model.SettleUp;
import java.util.List;
import java.util.UUID;

public interface IBalanceRepository {
    BalanceSheet saveBalanceSheet(BalanceSheet balanceSheet);
    BalanceSheet findBalanceSheetByGroup(Group group);
    SettleUp saveSettlement(SettleUp settleUp);
    List<SettleUp> findSettlementsByGroup(Group group);
}