package com.lld.loggingsystem.util.observer;

import com.lld.loggingsystem.model.LogMessage;

/**
 * Observer interface for the Observer pattern
 */
public interface LogObserver {
    void update(LogMessage logMessage);
}