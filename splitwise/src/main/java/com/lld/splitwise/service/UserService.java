package com.lld.splitwise.service;

import com.lld.splitwise.exception.UserNotFoundException;
import com.lld.splitwise.model.Member;
import com.lld.splitwise.repository.impl.MemberRepository;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final MemberRepository memberRepository;
    
    private UserService() {
        memberRepository = MemberRepository.getInstance();
    }
    
    public Member createUser(String name, String email, String phoneNumber) {
        Member member = Member.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        return memberRepository.save(member);
    }
    
    public Member getUser(UUID id) {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return member;
    }
    
    public Member getUserByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return member;
    }
    
    public List<Member> getAllUsers() {
        return memberRepository.findAll();
    }
    
    public void deleteUser(UUID id) {
        memberRepository.delete(id);
    }

    /**
     * Object Creation
     */

    private static UserService instance;

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
}