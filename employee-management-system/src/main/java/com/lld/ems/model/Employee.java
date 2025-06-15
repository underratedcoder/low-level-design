package com.lld.ems.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Employee {
    private final String employeeId;
    private String name;
    private String email;
    private String department;
    private String position;
    private BigDecimal salary;
    private String managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Employee(String employeeId, String name, String email, String department, 
                   String position, BigDecimal salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public BigDecimal getSalary() { return salary; }
    public String getManagerId() { return managerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Setters
    public void setName(String name) { 
        this.name = name; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setEmail(String email) { 
        this.email = email; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setDepartment(String department) { 
        this.department = department; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setPosition(String position) { 
        this.position = position; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setSalary(BigDecimal salary) { 
        this.salary = salary; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setManagerId(String managerId) { 
        this.managerId = managerId; 
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("Employee{id='%s', name='%s', position='%s', salary=%s, managerId='%s'}", 
                           employeeId, name, position, salary, managerId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(employeeId, employee.employeeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}