package com.lld.loggingsystem.util.observer;

import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.enums.LogSinkType;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.repository.ILogSink;
import com.lld.loggingsystem.util.factory.LogSinkFactory;

import java.util.Set;

/**
 * Observer implementation that routes logs to appropriate destinations
 */
public class LogDestinationObserver implements LogObserver {
    
    private final LogConfig logConfig;
    
    public LogDestinationObserver(LogConfig logConfig) {
        this.logConfig = logConfig;
    }
    
    @Override
    public void update(LogMessage logMessage) {
        Set<LogSinkType> logSinkTypes = logConfig.getDestinationsForLevel(logMessage.getLevel());
        
        for (LogSinkType logSinkType : logSinkTypes) {
            ILogSink logSink = LogSinkFactory.getRepository(logSinkType);
            logSink.writeLog(logMessage);
        }
    }
}