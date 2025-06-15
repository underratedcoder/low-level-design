// SplitStrategy.java
package com.lld.splitwise.util.strategy;

import com.lld.splitwise.model.Member;
import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    Map<Member, Double> calculateSplit(double amount, List<Member> members, Map<Member, Double> splitDetails);
}
