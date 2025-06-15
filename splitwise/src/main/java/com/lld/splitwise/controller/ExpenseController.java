// ExpenseController.java
package com.lld.splitwise.controller;

import com.lld.splitwise.enums.SplitType;
import com.lld.splitwise.model.Expense;
import com.lld.splitwise.service.ExpenseManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExpenseController {
    private final ExpenseManager expenseManager;
    
    public ExpenseController() {
        this.expenseManager = ExpenseManager.getInstance();
    }
    
    public Expense createExpense(UUID groupId, UUID payerId, double amount, String description, 
                              SplitType splitType, List<UUID> participantIds, Map<UUID, Double> splitDetails) {
        return expenseManager.createExpense(groupId, payerId, amount, description, 
                                          splitType, participantIds, splitDetails);
    }
    
    public Expense getExpense(UUID id) {
        return expenseManager.getExpense(id);
    }
    
    public List<Expense> getExpensesByGroup(UUID groupId) {
        return expenseManager.getExpensesByGroup(groupId);
    }
    
    public List<Expense> getExpensesByUser(UUID userId) {
        return expenseManager.getExpensesByUser(userId);
    }
}
