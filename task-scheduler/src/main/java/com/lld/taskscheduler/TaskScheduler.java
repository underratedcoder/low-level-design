package com.lld.taskscheduler;

import com.lld.taskscheduler.execution.TaskExecutor;
import com.lld.taskscheduler.job.ExecutionContext;
import com.lld.taskscheduler.task.OneTimeTask;
import com.lld.taskscheduler.task.RecurringTask;
import com.lld.taskscheduler.task.ScheduledTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

class TaskScheduler {
    private final PriorityBlockingQueue<ScheduledTask> taskStore;

    private final List<TaskExecutor> taskExecutors;
    private final ExecutorService executorService;

    public TaskScheduler(int parallelism) {
        this.taskStore = new PriorityBlockingQueue<>(12, Comparator.comparingLong(ScheduledTask::getExecutionTime));
        this.taskExecutors = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(parallelism);

        for (int i = 0; i < parallelism; i++) {
            TaskExecutor taskExecutor = new TaskExecutor(taskStore);
            executorService.submit(taskExecutor);
            taskExecutors.add(taskExecutor);
        }
    }

    public void schedule(ExecutionContext executionContext, long executionMillis) {
        ScheduledTask scheduledTask = new OneTimeTask(executionContext, executionMillis);
        addTaskAndNotify(scheduledTask);
    }

    public void scheduleRecurring(ExecutionContext executionContext, long executionMillis, long intervalMillis) {
        ScheduledTask scheduledTask = new RecurringTask(executionContext, executionMillis, intervalMillis);
        addTaskAndNotify(scheduledTask);
    }

    private void addTaskAndNotify(ScheduledTask task) {
        taskStore.offer(task);
        for (TaskExecutor taskExecutor : taskExecutors) {
            synchronized (taskExecutor) {
                taskExecutor.notify();
            }
        }
    }

    public void shutdown() {
        taskExecutors.forEach(TaskExecutor::stop);
        executorService.shutdown();
    }
}