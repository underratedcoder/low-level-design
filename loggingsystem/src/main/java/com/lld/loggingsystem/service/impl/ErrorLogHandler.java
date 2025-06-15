package com.lld.loggingsystem.service.impl;

import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.enums.LogLevel;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.service.AbstractLogHandler;
import com.lld.loggingsystem.util.observer.LogSubject;

/**
 * Handler for ERROR level logs
 */
public class ErrorLogHandler extends AbstractLogHandler {
    
    public ErrorLogHandler(LogConfig config, LogSubject subject) {
        super(LogLevel.ERROR, config, subject);
    }
    
    @Override
    protected void processLog(LogMessage logMessage) {
        if (logMessage.getLevel() == LogLevel.ERROR) {
            logSubject.notifyObservers(logMessage);
        }
    }
}