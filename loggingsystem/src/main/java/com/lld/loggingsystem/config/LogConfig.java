package com.lld.loggingsystem.config;

import com.lld.loggingsystem.enums.LogSinkType;
import com.lld.loggingsystem.enums.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Configuration for the logging system
 */
@AllArgsConstructor
@Data
@Builder
public class LogConfig {
    private LogLevel logLevel;
    private Map<LogLevel, Set<LogSinkType>> sinkMapping;
    
    public LogConfig () {
        this.logLevel = LogLevel.INFO;
        this.sinkMapping = new HashMap<>();

        // INFO logs go to FILE only
        Set<LogSinkType> infoSinks = new HashSet<>();
        infoSinks.add(LogSinkType.FILE);
        sinkMapping.put(LogLevel.INFO, infoSinks);
        
        // WARN and ERROR logs go to CONSOLE, FILE, and DATABASE
        Set<LogSinkType> warnErrorSinks = new HashSet<>();
        warnErrorSinks.add(LogSinkType.CONSOLE);
        warnErrorSinks.add(LogSinkType.FILE);
        warnErrorSinks.add(LogSinkType.DATABASE);
        sinkMapping.put(LogLevel.WARN, warnErrorSinks);
        sinkMapping.put(LogLevel.ERROR, warnErrorSinks);
        
        // DEBUG logs go to CONSOLE only
        Set<LogSinkType> debugSinks = new HashSet<>();
        debugSinks.add(LogSinkType.CONSOLE);
        sinkMapping.put(LogLevel.DEBUG, debugSinks);
    }
    
    public LogLevel getLevel() {
        return logLevel;
    }
    
    public Set<LogSinkType> getSinksForLevel(LogLevel level) {
        return sinkMapping.getOrDefault(level, new HashSet<>());
    }
}