package com.lld.loggingsystem.util.observer;

import com.lld.loggingsystem.model.LogMessage;

/**
 * Subject interface for the Observer pattern
 */
public interface LogSubject {
    void registerObserver(LogObserver observer);
    void removeObserver(LogObserver observer);
    void notifyObservers(LogMessage logMessage);
}