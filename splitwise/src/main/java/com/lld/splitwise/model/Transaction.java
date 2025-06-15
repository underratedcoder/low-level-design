package com.lld.splitwise.model;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Transaction {
    private UUID id;
    private Member from;
    private Member to;
    private double amount;
    private Date transactionDate;
    private String description;
}