package com.lld.loggingsystem.service;

import com.lld.loggingsystem.enums.LogLevel;
import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.util.observer.LogSubject;

/**
 * Abstract base class for log handlers implementing the Chain of Responsibility pattern
 */
public abstract class AbstractLogHandler implements LogHandler {
    
    protected LogHandler nextHandler;
    protected LogLevel handlerLevel;
    protected LogConfig logConfig;
    protected LogSubject logSubject;
    
    public AbstractLogHandler(LogLevel level, LogConfig config, LogSubject subject) {
        this.handlerLevel = level;
        this.logConfig = config;
        this.logSubject = subject;
    }
    
    @Override
    public void setNext(LogHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    
    @Override
    public void handleLog(LogMessage logMessage) {
        if (logMessage.getLevel().isLevelEnabled(handlerLevel)) {
            processLog(logMessage);
        }
        
        if (nextHandler != null) {
            nextHandler.handleLog(logMessage);
        }
    }
    
    protected abstract void processLog(LogMessage logMessage);
}