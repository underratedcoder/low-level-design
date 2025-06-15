// TransactionRepository.java
package com.lld.splitwise.repository.impl;

import com.lld.splitwise.model.Member;
import com.lld.splitwise.model.Transaction;
import com.lld.splitwise.repository.ITransactionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionRepository implements ITransactionRepository {
    private static TransactionRepository instance;
    private final Map<UUID, Transaction> transactions;

    private TransactionRepository() {
        this.transactions = new HashMap<>();
    }

    public static synchronized TransactionRepository getInstance() {
        if (instance == null) {
            instance = new TransactionRepository();
        }
        return instance;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public Transaction findById(UUID id) {
        return transactions.get(id);
    }

    @Override
    public List<Transaction> findByUser(Member member) {
        return transactions.values().stream()
            .filter(transaction -> 
                transaction.getFrom().getId().equals(member.getId()) ||
                transaction.getTo().getId().equals(member.getId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }
}