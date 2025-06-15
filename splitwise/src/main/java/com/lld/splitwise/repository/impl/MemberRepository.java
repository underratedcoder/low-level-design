
// UserRepository.java
package com.lld.splitwise.repository.impl;

import com.lld.splitwise.model.Member;
import com.lld.splitwise.repository.IUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemberRepository implements IUserRepository {
    private static MemberRepository instance;
    private final Map<UUID, Member> users;

    private MemberRepository() {
        this.users = new HashMap<>();
    }

    public static synchronized MemberRepository getInstance() {
        if (instance == null) {
            instance = new MemberRepository();
        }
        return instance;
    }

    @Override
    public Member save(Member member) {
        if (member.getId() == null) {
            member.setId(UUID.randomUUID());
        }
        users.put(member.getId(), member);
        return member;
    }

    @Override
    public Member findById(UUID id) {
        return users.get(id);
    }

    @Override
    public Member findByEmail(String email) {
        return users.values().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Member> findAll() {
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}