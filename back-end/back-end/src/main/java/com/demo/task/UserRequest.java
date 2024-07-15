package com.demo.task;

import java.io.InputStream;
import java.util.Date;

public class UserRequest {
    private User user;
    private InputStream fileStream;
    private Date requestTime;

    // Constructor, getters, and setters
    public UserRequest(User user, InputStream fileStream, Date requestTime) {
        this.user = user;
        this.fileStream = fileStream;
        this.requestTime = requestTime;
    }

    public User getUser() {
        return user;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public Date getRequestTime() {
        return requestTime;
    }
}
