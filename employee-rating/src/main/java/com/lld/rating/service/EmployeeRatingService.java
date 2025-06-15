package com.lld.rating.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EmployeeRatingService {

    private Map<String, AtomicInteger> employeeRatingSum = new ConcurrentHashMap<>();
    private Map<String, AtomicInteger> employeeRatingCount = new ConcurrentHashMap<>();
    private Map<String, Double> employeeRatings = new ConcurrentHashMap<>();

    public EmployeeRatingService() { }

    public void rate(String empId, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw  new RuntimeException("Invalid rating " + rating);
        }

        employeeRatingSum.computeIfAbsent(empId, k -> new AtomicInteger(0));
        employeeRatingCount.computeIfAbsent(empId, k -> new AtomicInteger(0));

        int ratingSum = employeeRatingSum.get(empId).addAndGet(rating);
        int ratingCount = employeeRatingCount.get(empId).incrementAndGet();

        Double avgRating = Double.parseDouble(String.format("%.3f", (double) ratingSum / ratingCount));

        employeeRatings.put(empId, avgRating);
    }

    public Double getAvgRating(String empId) {
        return employeeRatings.get(empId);
    }
}
