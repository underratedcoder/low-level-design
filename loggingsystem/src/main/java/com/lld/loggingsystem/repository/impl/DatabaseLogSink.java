package com.lld.loggingsystem.repository.impl;

import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.repository.ILogSink;

/**
 * Repository implementation for database logging
 */
public class DatabaseLogSink implements ILogSink {

    private DatabaseLogSink() {
        // In a real implementation, this would initialize database connection
    }
    
    @Override
    public void writeLog(LogMessage logMessage) {
        // In a real implementation, this would write to a database
        System.out.println(logMessage.toString() + " << DB LOG >>");
    }

    // Singleton pattern
    private static DatabaseLogSink instance;

    public static synchronized DatabaseLogSink getInstance() {
        if (instance == null) {
            instance = new DatabaseLogSink();
        }
        return instance;
    }
}