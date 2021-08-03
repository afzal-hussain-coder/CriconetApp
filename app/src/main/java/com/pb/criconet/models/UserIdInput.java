package com.pb.criconet.models;

public class UserIdInput {

    private String userId;
    private String sessionid;

    public UserIdInput(String userId, String sessionid) {
        this.userId = userId;
        this.sessionid = sessionid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
