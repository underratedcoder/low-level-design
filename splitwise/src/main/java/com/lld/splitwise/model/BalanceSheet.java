// BalanceSheet.java
package com.lld.splitwise.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class BalanceSheet {
    private UUID id;
    private Group group;
    private Map<Member, Map<Member, Double>> userBalances; // Who owes whom how much
}