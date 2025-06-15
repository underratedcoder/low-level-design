package com.lld.taskscheduler.execution;

import com.lld.taskscheduler.task.RecurringTask;
import com.lld.taskscheduler.task.ScheduledTask;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskExecutor implements Runnable {

    private final PriorityBlockingQueue<ScheduledTask> taskStore;

    private volatile boolean running;

    public TaskExecutor(PriorityBlockingQueue<ScheduledTask> taskStore) {
        this.taskStore = taskStore;
        this.running = true;
    }

    public void run() {
        while (running) {
            try {
                ScheduledTask scheduledTask = taskStore.take(); // Block until a task is available
                long delay = scheduledTask.getExecutionTime() - Instant.now().toEpochMilli();
                if (delay > 0) {
                    // Re-queue the task and wait until it is ready
                    taskStore.put(scheduledTask);
                    synchronized (this) {
                        wait(delay);
                    }
                } else {
                    scheduledTask.execute();
                    if (scheduledTask.isRecurring()) {
                        RecurringTask prevTask = (RecurringTask) scheduledTask;
                        ScheduledTask nextTask =
                                new RecurringTask(prevTask.context, prevTask.getExecutionTime() + prevTask.getInterval(), prevTask.getInterval());
                        taskStore.put(nextTask);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupted status
            }
        }
    }

    public void stop() {
        this.running = false;
        synchronized (this) {
            notify(); // Notify the thread to wake up and stop
        }
    }
}