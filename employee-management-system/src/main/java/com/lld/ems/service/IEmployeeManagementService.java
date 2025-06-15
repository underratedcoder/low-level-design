package com.lld.ems.service;

import com.lld.ems.model.Employee;
import com.lld.ems.model.ManagerBudget;

import java.math.BigDecimal;
import java.util.List;

interface IEmployeeManagementService {
    // Employee
    void addEmployee(Employee employee) throws Exception;
    void updateEmployee(Employee employee) throws Exception;
    void removeEmployee(String employeeId) throws Exception;
    Employee getEmployee(String employeeId) throws Exception;

    // Hierarchy
    void assignManager(String employeeId, String managerId) throws Exception;
    List<Employee> getDirectReports(String managerId) throws Exception;
    List<Employee> getAllReports(String managerId) throws Exception;
    List<String> getReportingLine(String employeeId) throws Exception;

    // BudgetDAO
    void setBudget(String managerId, BigDecimal budget) throws Exception;
    ManagerBudget getBudget(String managerId) throws Exception;
}