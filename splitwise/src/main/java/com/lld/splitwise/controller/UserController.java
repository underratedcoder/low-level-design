// UserController.java
package com.lld.splitwise.controller;

import com.lld.splitwise.model.Member;
import com.lld.splitwise.service.UserService;
import java.util.List;
import java.util.UUID;

public class UserController {
    private final UserService userService;
    
    public UserController() {
        this.userService = UserService.getInstance();
    }
    
    public Member createUser(String name, String email, String phoneNumber) {
        return userService.createUser(name, email, phoneNumber);
    }
    
    public Member getUser(UUID id) {
        return userService.getUser(id);
    }
    
    public Member getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }
    
    public List<Member> getAllUsers() {
        return userService.getAllUsers();
    }
    
    public void deleteUser(UUID id) {
        userService.deleteUser(id);
    }
}
