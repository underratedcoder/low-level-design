package com.lld.taskscheduler.task;

import com.lld.taskscheduler.job.ExecutionContext;

public abstract class ScheduledTask {
    public final ExecutionContext context;
    private final long executionTime;

    public ScheduledTask (ExecutionContext context, long executionTime) {
        this.context = context;
        this.executionTime = executionTime;
    }

    public void execute() throws InterruptedException {
        context.execute();
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public abstract boolean isRecurring();
}