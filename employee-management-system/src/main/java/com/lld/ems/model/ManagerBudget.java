package com.lld.ems.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ManagerBudget {
    private final String managerId;
    private BigDecimal totalBudget;
    private BigDecimal allocatedAmount;
    private LocalDateTime updatedAt;
    
    public ManagerBudget(String managerId, BigDecimal totalBudget) {
        this.managerId = managerId;
        this.totalBudget = totalBudget;
        this.allocatedAmount = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getManagerId() { return managerId; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public BigDecimal getAvailableBudget() { return totalBudget.subtract(allocatedAmount); }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public void setTotalBudget(BigDecimal totalBudget) { 
        this.totalBudget = totalBudget; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addAllocation(BigDecimal amount) {
        this.allocatedAmount = this.allocatedAmount.add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeAllocation(BigDecimal amount) {
        this.allocatedAmount = this.allocatedAmount.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
}