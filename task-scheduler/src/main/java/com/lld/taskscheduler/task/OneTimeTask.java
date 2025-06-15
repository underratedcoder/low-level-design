package com.lld.taskscheduler.task;

import com.lld.taskscheduler.job.ExecutionContext;

public class OneTimeTask extends ScheduledTask {

    public OneTimeTask(ExecutionContext context, long executionTime) {
        super(context, executionTime);
    }

    @Override
    public boolean isRecurring() {
        return false;
    }
}