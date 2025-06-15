package com.lld.taskscheduler;

import com.lld.taskscheduler.job.ExecutionContext;
import com.lld.taskscheduler.job.GoodMorningExecution;
import com.lld.taskscheduler.job.HelloExecution;
import com.lld.taskscheduler.job.HiExecution;

import java.time.LocalDateTime;

public class TaskSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Task Scheduler Created at " + LocalDateTime.now());

        TaskScheduler scheduler = new TaskScheduler(4);

        Thread.sleep(2000);

        ExecutionContext helloExecution = new HelloExecution();
        scheduler.scheduleRecurring(helloExecution, System.currentTimeMillis() + 15000, 2000);

        ExecutionContext hiExecution = new HiExecution();
        scheduler.scheduleRecurring(hiExecution, System.currentTimeMillis() + 15000, 2000);

        Thread.sleep(5000);

        ExecutionContext goodMorning = new GoodMorningExecution();
        scheduler.scheduleRecurring(goodMorning, System.currentTimeMillis() + 2000, 2000);

        Thread.sleep(20000);
        scheduler.shutdown();
    }

}