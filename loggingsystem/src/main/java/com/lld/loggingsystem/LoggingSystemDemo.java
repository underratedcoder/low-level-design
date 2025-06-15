package com.lld.loggingsystem;

import com.lld.loggingsystem.config.LogConfig;
import com.lld.loggingsystem.service.impl.LoggingService;

/**
 * Demo class to show the logging system in action
 */
public class LoggingSystemDemo {
    
    public static void main(String[] args) {

        LogConfig logConfig = new LogConfig();

        // Create controller
        LoggingService loggingService = LoggingService.getInstance(logConfig);

        // Log some messages
        loggingService.debug("This is a debug message");
        loggingService.info("This is an info message");
        loggingService.warn("This is a warning message");
        loggingService.error("This is an error message");
    }
}