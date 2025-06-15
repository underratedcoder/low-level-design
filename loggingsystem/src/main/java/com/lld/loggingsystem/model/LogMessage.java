package com.lld.loggingsystem.model;

import com.lld.loggingsystem.enums.LogLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a log message in the system
 */
@Data
@Builder
public class LogMessage {
    private String message;
    private LogLevel level;
    private LocalDateTime timestamp;
    private String threadName;
    
    @Override
    public String toString() {
        return String.format("[%s] [%s] [%s] - %s", timestamp, level, threadName, message);
    }
}