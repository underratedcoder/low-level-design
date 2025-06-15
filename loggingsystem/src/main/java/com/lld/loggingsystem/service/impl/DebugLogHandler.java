package com.lld.loggingsystem.service.impl;

import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.enums.LogLevel;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.service.AbstractLogHandler;
import com.lld.loggingsystem.util.observer.LogSubject;

/**
 * Handler for DEBUG level logs
 */
public class DebugLogHandler extends AbstractLogHandler {
    
    public DebugLogHandler(LogConfig config, LogSubject subject) {
        super(LogLevel.DEBUG, config, subject);
    }
    
    @Override
    protected void processLog(LogMessage logMessage) {
        if (logMessage.getLevel() == LogLevel.DEBUG) {
            logSubject.notifyObservers(logMessage);
        }
    }
}