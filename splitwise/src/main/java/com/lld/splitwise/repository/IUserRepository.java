// IUserRepository.java
package com.lld.splitwise.repository;

import com.lld.splitwise.model.Member;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    Member save(Member member);
    Member findById(UUID id);
    Member findByEmail(String email);
    List<Member> findAll();
    void delete(UUID id);
}