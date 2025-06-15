package com.lld.loggingsystem.repository.impl;

import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.repository.ILogSink;

/**
 * Repository implementation for console logging
 */
public class ConsoleLogSink implements ILogSink {
    
    private ConsoleLogSink() {}

    
    @Override
    public void writeLog(LogMessage logMessage) {
        System.out.println( logMessage.toString() + " << CONSOLE LOG >>");
    }

    // Singleton pattern
    private static ConsoleLogSink instance;

    public static synchronized ConsoleLogSink getInstance() {
        if (instance == null) {
            instance = new ConsoleLogSink();
        }
        return instance;
    }
}