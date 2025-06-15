package com.lld.ems.service;

import com.lld.ems.model.Employee;
import com.lld.ems.model.ManagerBudget;

import java.math.BigDecimal;
import java.util.*;

/**
 * You can create 3 separate DAO's later -
 * 1. EmployeeDAO
 * 2. HierarchyDAO
 * 3. BudgetDAO
 * */

public class EmployeeManagementService implements IEmployeeManagementService {
    private final Map<String, Employee> employees;
    private final Map<String, Set<String>> managerToReports;
    private final Map<String, ManagerBudget> managerBudgets;
    
    public EmployeeManagementService() {
        this.employees = new HashMap<>();
        this.managerToReports = new HashMap<>();
        this.managerBudgets = new HashMap<>();
    }
    
    @Override
    public void addEmployee(Employee employee) throws Exception {
        if (employees.containsKey(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee already exists: " + employee.getEmployeeId());
        }
        
        // Check budget constraint if employee has a manager
        if (employee.getManagerId() != null) {
            validateBudgetConstraint(employee.getManagerId(), employee.getSalary(), null);
        }
        
        employees.put(employee.getEmployeeId(), employee);
        
        // Update manager-reports relationship
        if (employee.getManagerId() != null) {
            managerToReports.computeIfAbsent(employee.getManagerId(), k -> new HashSet<>())
                           .add(employee.getEmployeeId());
            
            // Update budget allocation
            ManagerBudget budget = managerBudgets.get(employee.getManagerId());
            if (budget != null) {
                budget.addAllocation(employee.getSalary());
            }
        }
    }
    
    @Override
    public void updateEmployee(Employee updatedEmployee) throws Exception {
        Employee existingEmployee = getEmployee(updatedEmployee.getEmployeeId());
        
        BigDecimal oldSalary = existingEmployee.getSalary();
        String oldManagerId = existingEmployee.getManagerId();
        
        // Validate budget if salary or manager changed
        if (!updatedEmployee.getSalary().equals(oldSalary) || 
            !Objects.equals(updatedEmployee.getManagerId(), oldManagerId)) {
            
            if (updatedEmployee.getManagerId() != null) {
                validateBudgetConstraint(updatedEmployee.getManagerId(), 
                                       updatedEmployee.getSalary(), oldSalary);
            }
        }
        
        // Update budget allocations
        if (oldManagerId != null) {
            ManagerBudget oldBudget = managerBudgets.get(oldManagerId);
            if (oldBudget != null) {
                oldBudget.removeAllocation(oldSalary);
            }
            managerToReports.get(oldManagerId).remove(updatedEmployee.getEmployeeId());
        }
        
        if (updatedEmployee.getManagerId() != null) {
            ManagerBudget newBudget = managerBudgets.get(updatedEmployee.getManagerId());
            if (newBudget != null) {
                newBudget.addAllocation(updatedEmployee.getSalary());
            }
            managerToReports.computeIfAbsent(updatedEmployee.getManagerId(), k -> new HashSet<>())
                           .add(updatedEmployee.getEmployeeId());
        }
        
        employees.put(updatedEmployee.getEmployeeId(), updatedEmployee);
    }
    
    @Override
    public void removeEmployee(String employeeId) throws Exception {
        Employee employee = getEmployee(employeeId);
        
        // Check if employee has direct reports
        Set<String> directReports = managerToReports.get(employeeId);
        if (directReports != null && !directReports.isEmpty()) {
            throw new IllegalStateException("Cannot remove employee with direct reports: " + employeeId);
        }
        
        // Update manager's budget
        if (employee.getManagerId() != null) {
            ManagerBudget budget = managerBudgets.get(employee.getManagerId());
            if (budget != null) {
                budget.removeAllocation(employee.getSalary());
            }
            managerToReports.get(employee.getManagerId()).remove(employeeId);
        }
        
        employees.remove(employeeId);
        managerToReports.remove(employeeId);
        managerBudgets.remove(employeeId);
    }
    
    @Override
    public Employee getEmployee(String employeeId) throws Exception {
        Employee employee = employees.get(employeeId);
        if (employee == null) {
            throw new Exception("Employee not found: " + employeeId);
        }
        return employee;
    }
    
    @Override
    public void assignManager(String employeeId, String managerId) 
            throws Exception {
        
        Employee employee = getEmployee(employeeId);
        
        if (managerId != null) {
            getEmployee(managerId); // Validate manager exists
            
            // Check for circular reference
            if (wouldCreateCircularReference(employeeId, managerId)) {
                throw new Exception("Assignment would create circular reference");
            }
            
            // Validate budget constraint
            validateBudgetConstraint(managerId, employee.getSalary(), null);
        }
        
        String oldManagerId = employee.getManagerId();
        
        // Update relationships
        if (oldManagerId != null) {
            managerToReports.get(oldManagerId).remove(employeeId);
            ManagerBudget oldBudget = managerBudgets.get(oldManagerId);
            if (oldBudget != null) {
                oldBudget.removeAllocation(employee.getSalary());
            }
        }
        
        employee.setManagerId(managerId);
        
        if (managerId != null) {
            managerToReports.computeIfAbsent(managerId, k -> new HashSet<>()).add(employeeId);
            ManagerBudget newBudget = managerBudgets.get(managerId);
            if (newBudget != null) {
                newBudget.addAllocation(employee.getSalary());
            }
        }
    }
    
    @Override
    public List<Employee> getDirectReports(String managerId) throws Exception {
        getEmployee(managerId); // Validate manager exists
        
        Set<String> reportIds = managerToReports.getOrDefault(managerId, new HashSet<>());
        return reportIds.stream()
                       .map(employees::get)
                       .filter(Objects::nonNull)
                       .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public List<Employee> getAllReports(String managerId) throws Exception {
        getEmployee(managerId); // Validate manager exists
        
        List<Employee> allReports = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(managerId);
        
        while (!queue.isEmpty()) {
            String currentManagerId = queue.poll();
            Set<String> directReports = managerToReports.getOrDefault(currentManagerId, new HashSet<>());
            
            for (String reportId : directReports) {
                Employee report = employees.get(reportId);
                if (report != null) {
                    allReports.add(report);
                    queue.offer(reportId);
                }
            }
        }
        
        return allReports;
    }
    
    @Override
    public List<String> getReportingLine(String employeeId) throws Exception {
        getEmployee(employeeId); // Validate employee exists
        
        List<String> reportingLine = new ArrayList<>();
        String currentId = employeeId;
        Set<String> visited = new HashSet<>();
        
        while (currentId != null && !visited.contains(currentId)) {
            visited.add(currentId);
            reportingLine.add(currentId);
            
            Employee current = employees.get(currentId);
            currentId = (current != null) ? current.getManagerId() : null;
        }
        
        return reportingLine;
    }
    
    @Override
    public void setBudget(String managerId, BigDecimal budget) throws Exception {
        getEmployee(managerId); // Validate manager exists
        
        ManagerBudget managerBudget = managerBudgets.get(managerId);
        if (managerBudget == null) {
            managerBudget = new ManagerBudget(managerId, budget);
            managerBudgets.put(managerId, managerBudget);
        } else {
            managerBudget.setTotalBudget(budget);
        }
    }
    
    @Override
    public ManagerBudget getBudget(String managerId) throws Exception {
        getEmployee(managerId); // Validate manager exists
        
        return managerBudgets.computeIfAbsent(managerId, 
            k -> new ManagerBudget(k, BigDecimal.ZERO));
    }
    
    // Utility methods
    private void validateBudgetConstraint(String managerId, BigDecimal newSalary, BigDecimal oldSalary) 
            throws Exception {
        
        ManagerBudget budget = managerBudgets.get(managerId);
        if (budget != null) {
            BigDecimal adjustment = newSalary;
            if (oldSalary != null) {
                adjustment = newSalary.subtract(oldSalary);
            }
            
            if (budget.getAvailableBudget().compareTo(adjustment) < 0) {
                throw new Exception(
                    String.format("Budget exceeded for manager %s. Available: %s, Required: %s", 
                                managerId, budget.getAvailableBudget(), adjustment));
            }
        }
    }
    
    private boolean wouldCreateCircularReference(String employeeId, String managerId) {
        Set<String> visited = new HashSet<>();
        String currentId = managerId;
        
        while (currentId != null && !visited.contains(currentId)) {
            if (currentId.equals(employeeId)) {
                return true;
            }
            
            visited.add(currentId);
            Employee current = employees.get(currentId);
            currentId = (current != null) ? current.getManagerId() : null;
        }
        
        return false;
    }
    
    // Additional utility methods for system management
    public void printHierarchy() {
        System.out.println("=== Employee Hierarchy ===");
        Set<String> rootEmployees = new HashSet<>();
        
        // Find root employees (those without managers)
        for (Employee emp : employees.values()) {
            if (emp.getManagerId() == null) {
                rootEmployees.add(emp.getEmployeeId());
            }
        }
        
        // Print hierarchy starting from root employees
        for (String rootId : rootEmployees) {
            printEmployeeHierarchy(rootId, 0);
        }
    }
    
    private void printEmployeeHierarchy(String employeeId, int level) {
        Employee emp = employees.get(employeeId);
        if (emp == null) return;
        
        String indent = "  ".repeat(level);
        System.out.printf("%s- %s (%s) - $%s%n", 
                         indent, emp.getName(), emp.getPosition(), emp.getSalary());
        
        Set<String> reports = managerToReports.getOrDefault(employeeId, new HashSet<>());
        for (String reportId : reports) {
            printEmployeeHierarchy(reportId, level + 1);
        }
    }
    
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployees", employees.size());
        stats.put("totalManagers", managerToReports.size());
        stats.put("totalBudgets", managerBudgets.size());
        
        BigDecimal totalSalaries = employees.values().stream()
                                           .map(Employee::getSalary)
                                           .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalSalaries", totalSalaries);
        
        BigDecimal totalBudgets = managerBudgets.values().stream()
                                               .map(ManagerBudget::getTotalBudget)
                                               .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalBudgetsAllocated", totalBudgets);
        
        return stats;
    }
}