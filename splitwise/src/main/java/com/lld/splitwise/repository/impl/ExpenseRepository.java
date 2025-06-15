package com.lld.splitwise.repository.impl;

import com.lld.splitwise.model.Expense;
import com.lld.splitwise.repository.IExpenseRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ExpenseRepository implements IExpenseRepository {
    private final Map<UUID, Expense> expenses;
    private final Map<UUID, List<Expense>> groupExpenses;

    private ExpenseRepository() {
        this.expenses = new HashMap<>();
        this.groupExpenses = new HashMap<>();
    }

    @Override
    public Expense save(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(UUID.randomUUID());
        }

        expenses.put(expense.getId(), expense);

        List<Expense> oldGroupExpense = groupExpenses.getOrDefault(expense.getGroup().getId(), new ArrayList<>());
        oldGroupExpense.add(expense);

        return expense;
    }

    @Override
    public Expense findById(UUID id) {
        return expenses.get(id);
    }

    @Override
    public List<Expense> findByGroup(UUID groupId) {
        return groupExpenses.get(groupId);
    }

    @Override
    public List<Expense> findByMember(UUID memberId) {
        return expenses.values().stream()
            .filter(expense -> expense.getPaidBy().getId().equals(memberId) ||
                   expense.getUserContributions().containsKey(memberId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Expense> findAll() {
        return expenses.values().stream().collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        expenses.remove(id);
    }

    /**
     * Object Creation
     * */

    private static ExpenseRepository instance;

    public static synchronized ExpenseRepository getInstance() {
        if (instance == null) {
            instance = new ExpenseRepository();
        }
        return instance;
    }
}