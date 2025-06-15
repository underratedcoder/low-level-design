package com.lld.filesystem.util;

import com.lld.filesystem.model.UserSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Map<username, UserSession>
    private static final Map<String, UserSession> sessions = new ConcurrentHashMap<>();

    // Set or update a session
    public static void setSession(String username, UserSession session) {
        sessions.put(username, session);
    }

    // Get a session by username
    public static UserSession getSession(String username) {
        return sessions.get(username);
    }

    // Remove a session (logout)
    public static void removeSession(String username) {
        sessions.remove(username);
    }

    // Optionally, get all sessions
    public static Map<String, UserSession> getAllSessions() {
        return sessions;
    }
}