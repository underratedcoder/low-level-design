package com.lld.ems;

import com.lld.ems.model.Employee;
import com.lld.ems.model.ManagerBudget;
import com.lld.ems.service.EmployeeManagementService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EmployeeHierarchyTestMain {
    private static final EmployeeManagementService system = new EmployeeManagementService();
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("EMPLOYEE HIERARCHY SYSTEM - COMPREHENSIVE TEST SUITE");
        System.out.println("=".repeat(80));
        
        try {
            // Test sequence
            testBasicEmployeeOperations();
            testHierarchyManagement();
            testBudgetManagement();
            testReportingFunctionality();
            testEdgeCasesAndExceptions();
            testComplexScenarios();
            testSystemStatistics();
            
            printFinalResults();
            
        } catch (Exception e) {
            System.err.println("Unexpected error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Test 1: Basic Employee Operations
    private static void testBasicEmployeeOperations() {
        printTestHeader("BASIC EMPLOYEE OPERATIONS");
        
        try {
            // Create employees
            Employee ceo = new Employee("CEO001", "John Smith", "john@company.com",
                                      "Executive", "Chief Executive Officer", new BigDecimal("250000"));
            Employee cto = new Employee("CTO001", "Sarah Johnson", "sarah@company.com", 
                                      "Technology", "Chief Technology Officer", new BigDecimal("200000"));
            Employee cfo = new Employee("CFO001", "Michael Brown", "michael@company.com", 
                                      "Finance", "Chief Financial Officer", new BigDecimal("195000"));
            
            // Test adding employees
            system.addEmployee(ceo);
            system.addEmployee(cto);
            system.addEmployee(cfo);
            
            assertTest("Add CEO", system.getEmployee("CEO001").getName().equals("John Smith"));
            assertTest("Add CTO", system.getEmployee("CTO001").getName().equals("Sarah Johnson"));
            assertTest("Add CFO", system.getEmployee("CFO001").getName().equals("Michael Brown"));
            
            // Test updating employee
            cto.setName("Sarah Johnson-Wilson");
            cto.setSalary(new BigDecimal("210000"));
            system.updateEmployee(cto);
            
            assertTest("Update employee name", 
                      system.getEmployee("CTO001").getName().equals("Sarah Johnson-Wilson"));
            assertTest("Update employee salary", 
                      system.getEmployee("CTO001").getSalary().equals(new BigDecimal("210000")));
            
            // Test duplicate employee ID
            try {
                Employee duplicate = new Employee("CEO001", "Duplicate CEO", "dup@company.com", 
                                                "Executive", "CEO", new BigDecimal("100000"));
                system.addEmployee(duplicate);
                assertTest("Prevent duplicate employee ID", false);
            } catch (IllegalArgumentException e) {
                assertTest("Prevent duplicate employee ID", true);
            }
            
        } catch (Exception e) {
            System.err.println("Error in basic operations test: " + e.getMessage());
        }
    }
    
    // Test 2: Hierarchy Management
    private static void testHierarchyManagement() {
        printTestHeader("HIERARCHY MANAGEMENT");
        
        try {
            // Create additional employees for hierarchy
            Employee devManager = new Employee("DEV001", "Alice Davis", "alice@company.com", 
                                             "Technology", "Development Manager", new BigDecimal("130000"));
            Employee qaManager = new Employee("QA001", "Bob Wilson", "bob@company.com", 
                                            "Technology", "QA Manager", new BigDecimal("125000"));
            Employee seniorDev1 = new Employee("SEN001", "Charlie Lee", "charlie@company.com", 
                                             "Technology", "Senior Developer", new BigDecimal("105000"));
            Employee seniorDev2 = new Employee("SEN002", "Diana Chen", "diana@company.com", 
                                             "Technology", "Senior Developer", new BigDecimal("108000"));
            Employee juniorDev1 = new Employee("JUN001", "Eve Martinez", "eve@company.com", 
                                             "Technology", "Junior Developer", new BigDecimal("75000"));
            Employee juniorDev2 = new Employee("JUN002", "Frank Garcia", "frank@company.com", 
                                             "Technology", "Junior Developer", new BigDecimal("72000"));
            
            system.addEmployee(devManager);
            system.addEmployee(qaManager);
            system.addEmployee(seniorDev1);
            system.addEmployee(seniorDev2);
            system.addEmployee(juniorDev1);
            system.addEmployee(juniorDev2);
            
            // Build hierarchy
            system.assignManager("CTO001", "CEO001");  // CTO reports to CEO
            system.assignManager("CFO001", "CEO001");  // CFO reports to CEO
            system.assignManager("DEV001", "CTO001");  // Dev Manager reports to CTO
            system.assignManager("QA001", "CTO001");   // QA Manager reports to CTO
            system.assignManager("SEN001", "DEV001");  // Senior Dev 1 reports to Dev Manager
            system.assignManager("SEN002", "DEV001");  // Senior Dev 2 reports to Dev Manager
            system.assignManager("JUN001", "SEN001");  // Junior Dev 1 reports to Senior Dev 1
            system.assignManager("JUN002", "SEN002");  // Junior Dev 2 reports to Senior Dev 2
            
            assertTest("Assign CTO to CEO", 
                      system.getEmployee("CTO001").getManagerId().equals("CEO001"));
            assertTest("Assign Dev Manager to CTO", 
                      system.getEmployee("DEV001").getManagerId().equals("CTO001"));
            
            // Test circular reference prevention
            try {
                system.assignManager("CEO001", "JUN001"); // This should fail
                assertTest("Prevent circular reference", false);
            } catch (Exception e) {
                assertTest("Prevent circular reference", true);
            }
            
            // Test direct reports
            List<Employee> ceoReports = system.getDirectReports("CEO001");
            assertTest("CEO direct reports count", ceoReports.size() == 2);
            
            List<Employee> ctoReports = system.getDirectReports("CTO001");
            assertTest("CTO direct reports count", ctoReports.size() == 2);
            
            // Test all reports (recursive)
            List<Employee> allCtoReports = system.getAllReports("CTO001");
            assertTest("CTO all reports count", allCtoReports.size() == 6); // All under CTO
            
        } catch (Exception e) {
            System.err.println("Error in hierarchy management test: " + e.getMessage());
        }
    }
    
    // Test 3: Budget Management
    private static void testBudgetManagement() {
        printTestHeader("BUDGET MANAGEMENT");
        
        try {
            // Set budgets for managers
            system.setBudget("CEO001", new BigDecimal("1000000")); // CEO budget
            system.setBudget("CTO001", new BigDecimal("600000"));  // CTO budget
            system.setBudget("CFO001", new BigDecimal("200000"));  // CFO budget
            system.setBudget("DEV001", new BigDecimal("300000"));  // Dev Manager budget
            system.setBudget("QA001", new BigDecimal("150000"));   // QA Manager budget
            
            // Test budget retrieval
            ManagerBudget ceoBudget = system.getBudget("CEO001");
            assertTest("CEO budget total", ceoBudget.getTotalBudget().equals(new BigDecimal("1000000")));
            
            ManagerBudget ctoBudget = system.getBudget("CTO001");
            assertTest("CTO budget allocated > 0", ctoBudget.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0);
            
            // Test budget constraint enforcement
            try {
                Employee expensiveEmployee = new Employee("EXP001", "Expensive Developer", 
                                                        "expensive@company.com", "Technology", 
                                                        "Senior Developer", new BigDecimal("500000"));
                expensiveEmployee.setManagerId("DEV001");
                system.addEmployee(expensiveEmployee);
                assertTest("Budget constraint enforcement", false);
            } catch (Exception e) {
                assertTest("Budget constraint enforcement", true);
                System.out.println("    ✓ Budget constraint message: " + e.getMessage());
            }
            
            // Test budget updates when employee salary changes
            ManagerBudget devBudgetBefore = system.getBudget("DEV001");
            BigDecimal allocatedBefore = devBudgetBefore.getAllocatedAmount();
            
            Employee seniorDev = system.getEmployee("SEN001");
            seniorDev.setSalary(new BigDecimal("110000")); // Increase by 5000
            system.updateEmployee(seniorDev);
            
            ManagerBudget devBudgetAfter = system.getBudget("DEV001");
            BigDecimal allocatedAfter = devBudgetAfter.getAllocatedAmount();
            
            assertTest("Budget allocation update on salary change", 
                      allocatedAfter.subtract(allocatedBefore).equals(new BigDecimal("5000")));
            
        } catch (Exception e) {
            System.err.println("Error in budget management test: " + e.getMessage());
        }
    }
    
    // Test 4: Reporting Functionality
    private static void testReportingFunctionality() {
        printTestHeader("REPORTING FUNCTIONALITY");
        
        try {
            // Test reporting line
            List<String> juniorDevReportingLine = system.getReportingLine("JUN001");
            assertTest("Junior dev reporting line length", juniorDevReportingLine.size() == 4);
            assertTest("Junior dev reports to senior dev", 
                      juniorDevReportingLine.get(1).equals("SEN001"));
            assertTest("Reporting line ends with CEO", 
                      juniorDevReportingLine.get(juniorDevReportingLine.size() - 1).equals("CEO001"));
            
            System.out.println("    ✓ Complete reporting line for JUN001: " + juniorDevReportingLine);
            
            // Test CEO reporting line (should only contain CEO)
            List<String> ceoReportingLine = system.getReportingLine("CEO001");
            assertTest("CEO reporting line", ceoReportingLine.size() == 1 && 
                      ceoReportingLine.get(0).equals("CEO001"));
            
            // Test reporting line for different levels
            List<String> devManagerReportingLine = system.getReportingLine("DEV001");
            assertTest("Dev manager reporting line", devManagerReportingLine.size() == 3);
            
        } catch (Exception e) {
            System.err.println("Error in reporting functionality test: " + e.getMessage());
        }
    }
    
    // Test 5: Edge Cases and Exceptions
    private static void testEdgeCasesAndExceptions() {
        printTestHeader("EDGE CASES AND EXCEPTIONS");
        
        try {
            // Test employee not found
            try {
                system.getEmployee("NONEXISTENT");
                assertTest("Employee not found exception", false);
            } catch (Exception e) {
                assertTest("Employee not found exception", true);
            }
            
            // Test removing employee with reports (should fail)
            try {
                system.removeEmployee("DEV001"); // Has direct reports
                assertTest("Prevent removing manager with reports", false);
            } catch (IllegalStateException e) {
                assertTest("Prevent removing manager with reports", true);
            }
            
            // Test removing leaf employee (should succeed)
            system.removeEmployee("JUN002");
            try {
                system.getEmployee("JUN002");
                assertTest("Remove leaf employee", false);
            } catch (Exception e) {
                assertTest("Remove leaf employee", true);
            }
            
            // Test assigning non-existent manager
            try {
                system.assignManager("SEN001", "NONEXISTENT");
                assertTest("Assign non-existent manager", false);
            } catch (Exception e) {
                assertTest("Assign non-existent manager", true);
            }
            
            // Test getting reports for non-manager
            List<Employee> leafReports = system.getDirectReports("JUN001");
            assertTest("No reports for leaf employee", leafReports.isEmpty());
            
        } catch (Exception e) {
            System.err.println("Error in edge cases test: " + e.getMessage());
        }
    }
    
    // Test 6: Complex Scenarios
    private static void testComplexScenarios() {
        printTestHeader("COMPLEX SCENARIOS");
        
        try {
            // Scenario 1: Reorganization - Move entire team to new manager
            Employee newManager = new Employee("NEW001", "New Manager", "newmgr@company.com", 
                                             "Technology", "Technical Manager", new BigDecimal("135000"));
            system.addEmployee(newManager);
            system.setBudget("NEW001", new BigDecimal("400000"));
            system.assignManager("NEW001", "CTO001");
            
            // Move SEN002 and their reports to new manager
            system.assignManager("SEN002", "NEW001");
            
            assertTest("Reorganization - employee moved", 
                      system.getEmployee("SEN002").getManagerId().equals("NEW001"));
            
            List<Employee> newManagerReports = system.getDirectReports("NEW001");
            assertTest("New manager has reports", newManagerReports.size() == 1);
            
            // Scenario 2: Salary adjustment within budget
            Employee seniorDev2 = system.getEmployee("SEN002");
            BigDecimal oldSalary = seniorDev2.getSalary();
            seniorDev2.setSalary(new BigDecimal("115000")); // Increase salary
            system.updateEmployee(seniorDev2);
            
            assertTest("Salary increase within budget", 
                      system.getEmployee("SEN002").getSalary().equals(new BigDecimal("115000")));
            
            // Scenario 3: Check budget impact
            ManagerBudget newMgrBudget = system.getBudget("NEW001");
            assertTest("Budget updated after reorganization", 
                      newMgrBudget.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0);
            
        } catch (Exception e) {
            System.err.println("Error in complex scenarios test: " + e.getMessage());
        }
    }
    
    // Test 7: System Statistics
    private static void testSystemStatistics() {
        printTestHeader("SYSTEM STATISTICS");
        
        try {
            Map<String, Object> stats = system.getSystemStats();
            
            System.out.println("Current System Statistics:");
            stats.forEach((key, value) -> System.out.println("    " + key + ": " + value));
            
            assertTest("System has employees", ((Integer) stats.get("totalEmployees")) > 0);
            assertTest("System has managers", ((Integer) stats.get("totalManagers")) > 0);
            assertTest("System has budgets", ((Integer) stats.get("totalBudgets")) > 0);
            
            BigDecimal totalSalaries = (BigDecimal) stats.get("totalSalaries");
            BigDecimal totalBudgets = (BigDecimal) stats.get("totalBudgetsAllocated");
            
            assertTest("Total salaries > 0", totalSalaries.compareTo(BigDecimal.ZERO) > 0);
            assertTest("Total budgets >= total salaries", totalBudgets.compareTo(totalSalaries) >= 0);
            
            // Display complete hierarchy
            System.out.println("\nComplete Organization Hierarchy:");
            system.printHierarchy();
            
        } catch (Exception e) {
            System.err.println("Error in system statistics test: " + e.getMessage());
        }
    }
    
    // Helper Methods
    private static void printTestHeader(String testName) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("TEST: " + testName);
        System.out.println("─".repeat(60));
    }
    
    private static void assertTest(String testDescription, boolean condition) {
        testsTotal++;
        if (condition) {
            testsPassed++;
            System.out.println("✓ PASS: " + testDescription);
        } else {
            System.out.println("✗ FAIL: " + testDescription);
        }
    }
    
    private static void printFinalResults() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("FINAL TEST RESULTS");
        System.out.println("=".repeat(80));
        System.out.printf("Tests Passed: %d/%d (%.1f%%)%n", 
                         testsPassed, testsTotal, 
                         (double) testsPassed / testsTotal * 100);
        
        if (testsPassed == testsTotal) {
            System.out.println("🎉 ALL TESTS PASSED! The Employee Hierarchy System is working correctly.");
        } else {
            System.out.println("⚠️  Some tests failed. Please review the implementation.");
        }
        
        System.out.println("\nKey Features Tested:");
        System.out.println("• Employee CRUD operations");
        System.out.println("• Hierarchy management and validation");
        System.out.println("• Budget allocation and constraints");
        System.out.println("• Reporting line generation");
        System.out.println("• Exception handling");
        System.out.println("• Complex organizational changes");
        System.out.println("• System statistics and monitoring");
        
        System.out.println("\n" + "=".repeat(80));
    }
}