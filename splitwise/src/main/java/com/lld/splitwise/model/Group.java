// Group.java
package com.lld.splitwise.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Group {
    private UUID id;
    private String name;
    private String description;
    private List<Member> members;
}