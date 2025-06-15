package com.lld.taskscheduler.task;

import com.lld.taskscheduler.job.ExecutionContext;

public class RecurringTask extends ScheduledTask {

    private final long interval;

    public RecurringTask(ExecutionContext context, long executionTime, long interval) {
        super(context, executionTime);
        this.interval = interval;
    }

    @Override
    public boolean isRecurring() {
        return true;
    }

    public long getInterval() {
        return interval;
    }
}