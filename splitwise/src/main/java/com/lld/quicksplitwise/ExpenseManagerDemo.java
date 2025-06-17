package com.lld.quicksplitwise;

import java.util.List;

public class ExpenseManagerDemo {
    public static void main(String[] args) {
        SplitwiseService service = new SplitwiseService();

        service.createGroup("g1", "Trip");

        User u1 = new User("u1", "Akash");
        User u2 = new User("u2", "John");
        User u3 = new User("u3", "Alice");

        service.addUserToGroup("g1", u1);
        service.addUserToGroup("g1", u2);
        service.addUserToGroup("g1", u3);

        service.addExpense("e1", "g1", u1, 300,
                List.of(new Split(u1, 0), new Split(u2, 0), new Split(u3, 0)),
                ExpenseType.EQUAL);

        service.showOptimizedSettlementPlan("g1");

       // service.showBalances(); // John & Alice owe Akash

//        service.settleUserInGroup("g1", "u1", "u2");
//        service.showBalances();
    }
}
