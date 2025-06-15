package com.lld.splitwise.util.strategy;

import com.lld.splitwise.model.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public Map<Member, Double> calculateSplit(double amount, List<Member> members, Map<Member, Double> splitDetails) {
        Map<Member, Double> result = new HashMap<>();
        double splitAmount = amount / members.size();
        
        for (Member member : members) {
            result.put(member, splitAmount);
        }
        
        return result;
    }
}