package com.lld.loggingsystem.service.impl;

import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.enums.LogLevel;
import com.lld.loggingsystem.model.LogMessage;
import com.lld.loggingsystem.service.LogHandler;
import com.lld.loggingsystem.util.observer.LogDestinationObserver;
import com.lld.loggingsystem.util.observer.LogObserver;
import com.lld.loggingsystem.util.observer.LogSubject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Main service for the logging system, implements both LogSubject and Singleton patterns
 */
public class LoggingService implements LogSubject {
    private final List<LogObserver> observers;
    private final LogConfig logConfig;
    private final LogHandler logHandlerChain;
    
    private LoggingService(LogConfig logConfig) {
        this.observers = new ArrayList<>();
        this.logConfig = logConfig;
        
        // Set up the chain of responsibility
        LogHandler debugHandler = new DebugLogHandler(logConfig, this);
        LogHandler infoHandler = new InfoLogHandler(logConfig, this);
        LogHandler warnHandler = new WarnLogHandler(logConfig, this);
        LogHandler errorHandler = new ErrorLogHandler(logConfig, this);
        
        debugHandler.setNext(infoHandler);
        infoHandler.setNext(warnHandler);
        warnHandler.setNext(errorHandler);
        
        this.logHandlerChain = debugHandler;
        
        // Register the destination observer
        registerObserver(new LogDestinationObserver(logConfig));
    }
    
    @Override
    public void registerObserver(LogObserver observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(LogObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(LogMessage logMessage) {
        for (LogObserver observer : observers) {
            observer.update(logMessage);
        }
    }
    
    public void log(String message, LogLevel level) {
        LogMessage logMessage = LogMessage.builder()
                .message(message)
                .level(level)
                .timestamp(LocalDateTime.now())
                .threadName(Thread.currentThread().getName())
                .build();

        logHandlerChain.handleLog(logMessage);

//        LogLevel configuredLevel = logConfig.getLevel();
//        if (level.isLevelEnabled(configuredLevel)) {
//            LogMessage logMessage = LogMessage.builder()
//                    .message(message)
//                    .level(level)
//                    .timestamp(LocalDateTime.now())
//                    .threadName(Thread.currentThread().getName())
//                    .build();
//
//            logHandlerChain.handleLog(logMessage);
//        }
    }
    
    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }
    
    public void info(String message) {
        log(message, LogLevel.INFO);
    }
    
    public void warn(String message) {
        log(message, LogLevel.WARN);
    }
    
    public void error(String message) {
        log(message, LogLevel.ERROR);
    }

    // Singleton pattern
    private static LoggingService instance;

    public static synchronized LoggingService getInstance(LogConfig logConfig) {
        if (instance == null) {
            instance = new LoggingService(logConfig);
        }
        return instance;
    }
}