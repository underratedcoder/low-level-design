// ExpenseManager.java
package com.lld.splitwise.service;

import com.lld.splitwise.enums.SplitType;
import com.lld.splitwise.model.Member;
import com.lld.splitwise.util.factory.SplitStrategyFactory;
import com.lld.splitwise.model.Expense;
import com.lld.splitwise.model.Group;
import com.lld.splitwise.util.observer.ExpenseNotificationObserver;
import com.lld.splitwise.repository.impl.ExpenseRepository;
import com.lld.splitwise.util.strategy.SplitStrategy;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExpenseManager {
    private final ExpenseRepository expenseRepository;
    private final GroupManager groupManager;
    private final UserService userService;
    private final BalanceSheetManager balanceSheetManager;
    private final SplitStrategyFactory splitStrategyFactory;
    private final ExpenseNotificationObserver notificationObserver;
    
    private ExpenseManager() {
        expenseRepository = ExpenseRepository.getInstance();
        groupManager = GroupManager.getInstance();
        userService = UserService.getInstance();
        balanceSheetManager = BalanceSheetManager.getInstance();
        splitStrategyFactory = SplitStrategyFactory.getInstance();
        notificationObserver = ExpenseNotificationObserver.getInstance();
    }
    
    public Expense createExpense(UUID groupId, UUID payerId, double amount, String description, 
                                SplitType splitType, List<UUID> participantIds, Map<UUID, Double> splitDetails) {
        
        Group group = groupManager.getGroup(groupId);
        Member payer = userService.getUser(payerId);
        
        // Convert participant IDs to User objects
        List<Member> participants = participantIds.stream()
                .map(userService::getUser)
                .collect(Collectors.toList());
        
        // Convert splitDetails from UUID to User objects
        Map<Member, Double> userSplitDetails = splitDetails.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                    entry -> userService.getUser(entry.getKey()), 
                    Map.Entry::getValue
                ));
        
        // Use strategy pattern to calculate splits
        SplitStrategy strategy = splitStrategyFactory.createStrategy(splitType);
        Map<Member, Double> userContributions = strategy.calculateSplit(amount, participants, userSplitDetails);
        
        // Create the expense
        Expense expense = Expense.builder()
                .description(description)
                .amount(amount)
                .paidBy(payer)
                .group(group)
                .createdAt(new Date())
                .splitType(splitType)
                .userContributions(userContributions)
                .build();
        
        Expense savedExpense = expenseRepository.save(expense);
        
        // Update balance sheet
        balanceSheetManager.updateBalanceSheet(savedExpense);
        
        // Notify all participants
        for (Member member : participants) {
            if (!member.getId().equals(payerId)) {
                notificationObserver.notify(member, "You have been added to a new expense: " + description);
            }
        }
        
        return savedExpense;
    }
    
    public Expense getExpense(UUID id) {
        return expenseRepository.findById(id);
    }
    
    public List<Expense> getExpensesByGroup(UUID groupId) {
        return expenseRepository.findByGroup(groupId);
    }
    
    public List<Expense> getExpensesByUser(UUID memberId) {
        return expenseRepository.findByMember(memberId);
    }

    /**
     * Object Creation
     */

    private static ExpenseManager instance;

    public static synchronized ExpenseManager getInstance() {
        if (instance == null) {
            instance = new ExpenseManager();
        }
        return instance;
    }
}