package com.lld.rating.service;

import com.lld.rating.model.EmployeeRatingEntry;

import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class EmployeeRatingService {

    // All time rating data
    private final Map<String, AtomicInteger> employeeRatingSum = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> employeeRatingCount = new ConcurrentHashMap<>();
    private final Map<String, Double> employeeRatingsAvg = new ConcurrentHashMap<>();
    private final NavigableSet<EmployeeRatingEntry> sortedRatings =
            new ConcurrentSkipListSet<>((e1, e2) -> {
                int cmp = Double.compare(e2.getAverageRating(), e1.getAverageRating());
                return cmp != 0 ? cmp : e1.getEmployeeId().compareTo(e2.getEmployeeId());
            });

    // Month-wise rating data
    private final Map<YearMonth, Map<String, AtomicInteger>> employeeRatingSumMonthly = new ConcurrentHashMap<>();
    private final Map<YearMonth, Map<String, AtomicInteger>> employeeRatingCountMonthly = new ConcurrentHashMap<>();
    private final Map<YearMonth, Map<String, Double>> employeeRatingsAvgMonthly = new ConcurrentHashMap<>();
    private final Map<YearMonth, NavigableSet<EmployeeRatingEntry>> sortedRatingsMonthly = new ConcurrentHashMap<>();

    public EmployeeRatingService() {

    }

    public void rate(String empId, int rating, YearMonth month) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5: " + rating);
        }

        // === GLOBAL RATINGS ===
        employeeRatingSum.computeIfAbsent(empId, k -> new AtomicInteger(0));
        employeeRatingCount.computeIfAbsent(empId, k -> new AtomicInteger(0));
        employeeRatingsAvg.putIfAbsent(empId, 0.0);

        int sum = employeeRatingSum.get(empId).addAndGet(rating);
        int count = employeeRatingCount.get(empId).incrementAndGet();

        double oldAvg = employeeRatingsAvg.get(empId);
        double newAvg = (double) sum / count;

        employeeRatingsAvg.put(empId, newAvg);

        sortedRatings.remove(new EmployeeRatingEntry(empId, oldAvg));
        sortedRatings.add(new EmployeeRatingEntry(empId, newAvg));

        // === MONTH-WISE RATINGS ===
        employeeRatingSumMonthly
                .computeIfAbsent(month, m -> new ConcurrentHashMap<>())
                .computeIfAbsent(empId, e -> new AtomicInteger(0));
        employeeRatingCountMonthly
                .computeIfAbsent(month, m -> new ConcurrentHashMap<>())
                .computeIfAbsent(empId, e -> new AtomicInteger(0));
        employeeRatingsAvgMonthly
                .computeIfAbsent(month, m -> new ConcurrentHashMap<>())
                .putIfAbsent(empId, 0.0);

        int monthSum = employeeRatingSumMonthly.get(month).get(empId).addAndGet(rating);
        int monthCount = employeeRatingCountMonthly.get(month).get(empId).get();

        double oldMonthAvg = employeeRatingsAvgMonthly.get(month).get(empId);
        double newMonthAvg = (double) monthSum / monthCount;

        employeeRatingsAvgMonthly.get(month).put(empId, newMonthAvg);

        NavigableSet<EmployeeRatingEntry> monthSet = sortedRatingsMonthly
                .computeIfAbsent(month, m -> new ConcurrentSkipListSet<>(
                        (e1, e2) -> {
                            int cmp = Double.compare(e2.getAverageRating(), e1.getAverageRating());
                            return cmp != 0 ? cmp : e1.getEmployeeId().compareTo(e2.getEmployeeId());
                        }
                ));

        monthSet.remove(new EmployeeRatingEntry(empId, oldMonthAvg));
        monthSet.add(new EmployeeRatingEntry(empId, newMonthAvg));
    }

    public void rate(String empId, int rating) {
        rate(empId, rating, YearMonth.now());
    }

    public double getAvgRating(String empId) {
        return employeeRatingsAvg.getOrDefault(empId, 0.0);
    }

    public String maxRatedEmp() {
        return sortedRatings.isEmpty() ? null : sortedRatings.first().getEmployeeId();
    }

    public List<EmployeeRatingEntry> getTopKAvgRating(int k) {
        return sortedRatings.stream().limit(k).toList();
    }

    public String getMonthTopRatedEmployee(YearMonth month) {
        NavigableSet<EmployeeRatingEntry> set = sortedRatingsMonthly.get(month);
        return (set == null || set.isEmpty()) ? null : set.first().getEmployeeId();
    }

    public List<EmployeeRatingEntry> getMonthTopKRatedEmployees(YearMonth month, int k) {
        return sortedRatingsMonthly.getOrDefault(month, Collections.emptyNavigableSet())
                .stream()
                .limit(k)
                .toList();
    }
}
