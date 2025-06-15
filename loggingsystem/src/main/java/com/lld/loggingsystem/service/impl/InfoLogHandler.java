package com.lld.loggingsystem.service.impl;

import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.enums.LogLevel;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.service.AbstractLogHandler;
import com.lld.loggingsystem.util.observer.LogSubject;

/**
 * Handler for INFO level logs
 */
public class InfoLogHandler extends AbstractLogHandler {
    
    public InfoLogHandler(LogConfig config, LogSubject subject) {
        super(LogLevel.INFO, config, subject);
    }
    
    @Override
    protected void processLog(LogMessage logMessage) {
        if (logMessage.getLevel() == LogLevel.INFO) {
            logSubject.notifyObservers(logMessage);
        }
    }
}