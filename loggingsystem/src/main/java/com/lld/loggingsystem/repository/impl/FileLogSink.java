package com.lld.loggingsystem.repository.impl;

import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.repository.ILogSink;

/**
 * Repository implementation for file logging
 */
public class FileLogSink implements ILogSink {

    public FileLogSink() {
        // In a real implementation, this would initialize database connection
    }
    
    @Override
    public void writeLog(LogMessage logMessage) {
        // In a real implementation, this would write to a file
        System.out.println(logMessage.toString() + " << FILE LOG >>");
    }

    // Singleton pattern
    private static FileLogSink instance;

    public static synchronized FileLogSink getInstance() {
        if (instance == null) {
            instance = new FileLogSink();
        }
        return instance;
    }
}