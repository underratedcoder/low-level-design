package com.lld.loggingsystem.service;

import com.lld.loggingsystem.model.LogMessage;

/**
 * Handler interface for the Chain of Responsibility pattern
 */
public interface LogHandler {
    void setNext(LogHandler nextHandler);
    void handleLog(LogMessage logMessage);
}