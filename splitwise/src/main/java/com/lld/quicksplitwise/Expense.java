package com.lld.quicksplitwise;

import lombok.Getter;

import java.util.List;

@Getter
public class Expense {
    private final String expenseId;
    private final User paidBy;
    private final double amount;
    private final List<Split> splits;
    private final ExpenseType type;

    public Expense(String expenseId, User paidBy, double amount, List<Split> splits, ExpenseType type) {
        this.expenseId = expenseId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.splits = splits;
        this.type = type;
    }
}
