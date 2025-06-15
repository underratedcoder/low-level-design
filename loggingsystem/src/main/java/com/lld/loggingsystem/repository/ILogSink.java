package com.lld.loggingsystem.repository;

import com.lld.loggingsystem.model.LogMessage;

/**
 * Interface for log repositories
 */
public interface ILogSink {
    void writeLog(LogMessage logMessage);
}