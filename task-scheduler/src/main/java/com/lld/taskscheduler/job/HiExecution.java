package com.lld.taskscheduler.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HiExecution implements ExecutionContext {
    public void execute() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = now.format(formatter);
        System.out.println("Hi at " + formatted + " by " + Thread.currentThread().getName());
        Thread.sleep(1000);
    }
}