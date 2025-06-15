package com.lld.ratelimiter.model;

public class UserRequest {
    int requestId;
    String userId;
    Long requestDateTime;

    public UserRequest(int requestId, String userId, Long requestDateTime) {
        this.requestId = requestId;
        this.userId = userId;
        this.requestDateTime = requestDateTime;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public Long getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRequestDateTime(Long requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
}
