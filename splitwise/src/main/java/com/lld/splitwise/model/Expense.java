package com.lld.splitwise.model;

import com.lld.splitwise.enums.SplitType;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class Expense {
    private UUID id;
    private String description;
    private double amount;
    private Member paidBy;
    private Group group;
    private Date createdAt;
    private SplitType splitType;
    private Map<Member, Double> userContributions; // either % or absolute values based on splitType
}