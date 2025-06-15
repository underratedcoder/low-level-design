package com.lld.splitwise.util.strategy;

import com.lld.splitwise.exception.InvalidSplitException;
import com.lld.splitwise.model.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PercentageSplitStrategy implements SplitStrategy {
    @Override
    public Map<Member, Double> calculateSplit(double amount, List<Member> members, Map<Member, Double> percentages) {
        Map<Member, Double> result = new HashMap<>();
        double totalPercentage = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        
        if (Math.abs(totalPercentage - 100.0) > 0.01) {
            throw new InvalidSplitException("Total percentage must be 100%");
        }
        
        for (Member member : members) {
            if (!percentages.containsKey(member)) {
                throw new InvalidSplitException("Percentage not provided for all users");
            }
            
            double userPercentage = percentages.get(member);
            double userAmount = (amount * userPercentage) / 100;
            result.put(member, userAmount);
        }
        
        return result;
    }
}