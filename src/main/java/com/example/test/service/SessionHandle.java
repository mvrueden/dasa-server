package com.example.test.service;

import java.util.Objects;

public class SessionHandle {
    private final String sessionId;
    private final long deviceId;

    public SessionHandle(final String sessionId, final long deviceId) {
        this.sessionId = Objects.requireNonNull(sessionId);
        this.deviceId = deviceId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getDeviceId() {
        return deviceId;
    }
}
