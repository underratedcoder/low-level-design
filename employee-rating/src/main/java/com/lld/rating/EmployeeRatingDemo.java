package com.lld.rating;


import com.lld.rating.service.EmployeeRatingService;

public class EmployeeRatingDemo {
    public static void main(String[] args) {
        EmployeeRatingService employeeRatingService = new EmployeeRatingService();

        String emp1 = "emp1";

        employeeRatingService.rate(emp1, 3);
        employeeRatingService.rate(emp1, 4);
        employeeRatingService.rate(emp1, 1);

        double avgRating = employeeRatingService.getAvgRating(emp1);

        System.out.println("Avg rating for " + emp1 + " is " + avgRating);
    }
}
