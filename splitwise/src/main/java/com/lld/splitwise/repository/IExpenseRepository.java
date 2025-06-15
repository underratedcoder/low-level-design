// IExpenseRepository.java
package com.lld.splitwise.repository;

import com.lld.splitwise.model.Expense;
import java.util.List;
import java.util.UUID;

public interface IExpenseRepository {
    Expense save(Expense expense);
    Expense findById(UUID id);
    List<Expense> findByGroup(UUID groupId);
    List<Expense> findByMember(UUID memberId);
    List<Expense> findAll();
    void delete(UUID id);
}