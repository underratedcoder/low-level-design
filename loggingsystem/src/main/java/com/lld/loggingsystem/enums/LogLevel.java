package com.lld.loggingsystem.enums;

/**
 * Enum representing different log levels in the logging system
 */
public enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isLevelEnabled(LogLevel configuredLevel) {
        return this.priority >= configuredLevel.getPriority();
    }
}