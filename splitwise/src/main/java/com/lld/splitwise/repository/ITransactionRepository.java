// ITransactionRepository.java
package com.lld.splitwise.repository;

import com.lld.splitwise.model.Transaction;
import com.lld.splitwise.model.Member;
import java.util.List;
import java.util.UUID;

public interface ITransactionRepository {
    Transaction save(Transaction transaction);
    Transaction findById(UUID id);
    List<Transaction> findByUser(Member member);
    List<Transaction> findAll();
}