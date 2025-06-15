package com.lld.loggingsystem.service.impl;

import com.lld.loggingsystem.enums.LogLevel;
import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.service.AbstractLogHandler;
import com.lld.loggingsystem.util.observer.LogSubject;

/**
 * Handler for WARN level logs
 */
public class WarnLogHandler extends AbstractLogHandler {
    
    public WarnLogHandler(LogConfig config, LogSubject subject) {
        super(LogLevel.WARN, config, subject);
    }
    
    @Override
    protected void processLog(LogMessage logMessage) {
        if (logMessage.getLevel() == LogLevel.WARN) {
            logSubject.notifyObservers(logMessage);
        }
    }
}