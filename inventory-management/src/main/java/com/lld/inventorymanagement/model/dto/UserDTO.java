package com.lld.inventorymanagement.model.dto;

import com.lld.inventorymanagement.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserDTO {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private UserRole role;
    private Date createdAt;
    private Date updatedAt;
}