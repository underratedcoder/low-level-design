package com.lld.taskscheduler.job;

public interface ExecutionContext {

    void execute() throws InterruptedException;
}