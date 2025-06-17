package com.lld.rating;


import com.lld.rating.model.EmployeeRatingEntry;
import com.lld.rating.service.EmployeeRatingService;

import java.time.YearMonth;
import java.util.List;

public class EmployeeRatingDemo {
    public static void main(String[] args) {
        EmployeeRatingService employeeRatingService = new EmployeeRatingService();

        String emp1 = "emp1";

        employeeRatingService.rate(emp1, 3);
        employeeRatingService.rate(emp1, 4);


        double avgRating = employeeRatingService.getAvgRating(emp1);

        System.out.println("Avg rating for " + emp1 + " is " + avgRating);

        employeeRatingService.rate(emp1, 5);
        avgRating = employeeRatingService.getAvgRating(emp1);

        System.out.println("Avg rating for " + emp1 + " is " + avgRating);

        String emp2 = "emp2";

        employeeRatingService.rate(emp2, 1);
        employeeRatingService.rate(emp2, 2);

        String emp3 = "emp3";

        employeeRatingService.rate(emp3, 2);
        employeeRatingService.rate(emp3, 3);

        for (int i = 1; i<=3; i++) {
            List<EmployeeRatingEntry> r = employeeRatingService.getTopKAvgRating(i);
            System.out.println("Top " + i + " Ratings...");
            r.stream().forEach(e -> System.out.println(e.getEmployeeId() + " : " + e.getAverageRating()));
        }

        // DEMO MONTH-WISE
        YearMonth yearMonth = YearMonth.of(2025, 6);
    }
}
