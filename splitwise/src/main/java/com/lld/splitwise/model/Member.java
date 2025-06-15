// User.java
package com.lld.splitwise.model;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class Member {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
}