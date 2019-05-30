package com.overops.plugins.runner.manager.model;

public class TestServiceResponse {
    private boolean status;
    private String message;

    public TestServiceResponse() {
    }

    public TestServiceResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static TestServiceResponse ok(String message) {
        return new TestServiceResponse(true, message);
    }

    public static TestServiceResponse fail(String message) {
        return new TestServiceResponse(false, message);
    }
}
