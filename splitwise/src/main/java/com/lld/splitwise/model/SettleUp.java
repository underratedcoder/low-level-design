// SettleUp.java
package com.lld.splitwise.model;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class SettleUp {
    private UUID id;
    private Member payer;
    private Member receiver;
    private double amount;
    private Date settlementDate;
    private Group group;
}