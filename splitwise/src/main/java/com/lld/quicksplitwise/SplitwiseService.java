package com.lld.quicksplitwise;

import java.util.*;

public class SplitwiseService {
    private final Map<String, Group> groupMap = new HashMap<>();
    private final Map<String, Map<String, Map<String, Double>>> groupBalanceSheet = new HashMap<>();
    private final Map<String, List<Expense>> groupExpenses = new HashMap<>();

    public void createGroup(String groupId, String name) {
        groupMap.put(groupId, new Group(groupId, name));
        groupExpenses.put(groupId, new ArrayList<>());
        groupBalanceSheet.put(groupId, new HashMap<>());
    }

    public void addUserToGroup(String groupId, User user) {
        Group group = groupMap.get(groupId);
        if (group != null) {
            group.addUser(user);
            groupBalanceSheet.putIfAbsent(user.getUserId(), new HashMap<>());
        }
    }

    public void addExpense(String expenseId, String groupId, User paidBy, double amount, List<Split> splits, ExpenseType type) {
        if (!groupMap.containsKey(groupId)) return;

        if (type == ExpenseType.EQUAL) {
            double splitAmount = amount / splits.size();
            for (int i = 0; i < splits.size(); i++) {
                splits.set(i, new Split(splits.get(i).getUser(), splitAmount));
            }
        } else if (type == ExpenseType.RATIO) {
            double totalSum = splits.stream().mapToDouble(Split::getAmount).sum();
            if (Math.abs(totalSum - amount) > 0.001) {
                throw new IllegalArgumentException("Split amount must match expense amount");
            }
        }

        for (Split split : splits) {
            if (!split.getUser().equals(paidBy)) {
                updateBalance(groupId, paidBy.getUserId(), split.getUser().getUserId(), split.getAmount());
            }
        }

        groupExpenses.get(groupId).add(new Expense(expenseId, paidBy, amount, splits, type));
    }

    private void updateBalance(String groupId, String paidBy, String owesUser, double amount) {
        Map<String, Map<String, Double>> balanceSheet = groupBalanceSheet.get(groupId);

        balanceSheet.putIfAbsent(paidBy, new HashMap<>());
        //balanceSheet.putIfAbsent(owesUser, new HashMap<>());

        balanceSheet.get(paidBy).put(owesUser,
                balanceSheet.get(paidBy).getOrDefault(owesUser, 0.0) + amount);

//        balanceSheet.get(owesUser).put(paidBy,
//                balanceSheet.get(owesUser).getOrDefault(paidBy, 0.0) - amount);
    }

    public void showBalances(String groupId) {
        Map<String, Map<String, Double>> balanceSheet = groupBalanceSheet.get(groupId);
        for (String user1 : balanceSheet.keySet()) {
            for (Map.Entry<String, Double> entry : balanceSheet.get(user1).entrySet()) {
                if (entry.getValue() > 0.0) {
                    System.out.println(user1 + " is owed " + entry.getValue() + " by " + entry.getKey());
                }
            }
        }
    }

    public void showOptimizedSettlementPlan(String groupId) {
        Map<String, Map<String, Double>> balanceSheet = groupBalanceSheet.get(groupId);

        Map<String, Double> netBalance = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entry : balanceSheet.entrySet()) {
            String fromUser = entry.getKey();
            for (Map.Entry<String, Double> toEntry : entry.getValue().entrySet()) {
                String toUser = toEntry.getKey();
                double amount = toEntry.getValue();

                if (amount > 0.001) {
                    netBalance.put(fromUser, netBalance.getOrDefault(fromUser, 0.0) + amount);
                    netBalance.put(toUser, netBalance.getOrDefault(toUser, 0.0) - amount);
                }
            }
        }

        PriorityQueue<Map.Entry<String, Double>> creditors = new PriorityQueue<>(Map.Entry.comparingByValue());
        PriorityQueue<Map.Entry<String, Double>> debtors = new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<String, Double> entry : netBalance.entrySet()) {
            if (entry.getValue() < 0) {
                debtors.add(Map.entry(entry.getKey(), -entry.getValue()));
            } else if (entry.getValue() > 0) {
                creditors.add(Map.entry(entry.getKey(), entry.getValue()));
            }
        }

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<String, Double> debtor = debtors.poll();
            Map.Entry<String, Double> creditor = creditors.poll();

            double amount = Math.min(debtor.getValue(), creditor.getValue());
            System.out.println(debtor.getKey() + " pays " + amount + " to " + creditor.getKey());

            double remDebt = debtor.getValue() - amount;
            double remCredit = creditor.getValue() - amount;

            if (remDebt > 0.001) debtors.add(Map.entry(debtor.getKey(), remDebt));
            if (remCredit > 0.001) creditors.add(Map.entry(creditor.getKey(), remCredit));
        }
    }

    public void settleUserInGroup(String groupId, String fromUser, String toUser, double paidAmount) {
        Map<String, Map<String, Double>> balanceSheet = groupBalanceSheet.get(groupId);

//        if (balanceSheet == null) {
//            System.out.println("Group not found.");
//            return;
//        }
//
//        if (!balanceSheet.containsKey(fromUser) || !balanceSheet.get(fromUser).containsKey(toUser)) {
//            System.out.println("No balance exists between " + fromUser + " and " + toUser + " in group " + groupId);
//            return;
//        }
//
//        double currentOwes = -balanceSheet.get(fromUser).getOrDefault(toUser, 0.0); // From user's perspective
//        if (currentOwes <= 0) {
//            System.out.println(fromUser + " does not owe anything to " + toUser);
//            return;
//        }
//
//        double paidAmount = Math.min(amount, currentOwes);

        // Update balances
        balanceSheet.get(fromUser).put(toUser, balanceSheet.get(fromUser).get(toUser) + paidAmount);
        //balanceSheet.get(toUser).put(fromUser, balanceSheet.get(toUser).get(fromUser) - paidAmount);

        System.out.println(fromUser + " paid " + paidAmount + " to " + toUser);

        // Optional: Clear near-zero balances
        if (Math.abs(balanceSheet.get(fromUser).get(toUser)) < 0.001) {
            balanceSheet.get(fromUser).put(toUser, 0.0);
        }
//        if (Math.abs(balanceSheet.get(toUser).get(fromUser)) < 0.001) {
//            balanceSheet.get(toUser).put(fromUser, 0.0);
//        }
    }


    public List<Expense> getExpensesForGroup(String groupId) {
        return groupExpenses.getOrDefault(groupId, Collections.emptyList());
    }
}