package com.lld.loggingsystem.util.factory;

import com.lld.loggingsystem.enums.LogSinkType;
import com.lld.loggingsystem.repository.ILogSink;
import com.lld.loggingsystem.repository.impl.ConsoleLogSink;
import com.lld.loggingsystem.repository.impl.FileLogSink;
import com.lld.loggingsystem.repository.impl.DatabaseLogSink;


/**
 * Factory for creating log repositories
 */
public class LogSinkFactory {
    
    public static ILogSink getRepository(LogSinkType destination) {
        switch (destination) {
            case CONSOLE:
                return ConsoleLogSink.getInstance();
            case FILE:
                return FileLogSink.getInstance();
            case DATABASE:
                return DatabaseLogSink.getInstance();
            default:
                throw new IllegalArgumentException("Unsupported log destination: " + destination);
        }
    }
}