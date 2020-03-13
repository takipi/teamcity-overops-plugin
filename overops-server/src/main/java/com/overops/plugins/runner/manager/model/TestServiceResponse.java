package com.overops.plugins.runner.manager.model;

public class TestServiceResponse {
    private final boolean status;
    private final String message;

    public TestServiceResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static TestServiceResponse ok(String message) {
        return new TestServiceResponse(true, message);
    }

    public static TestServiceResponse fail(String message) {
        return new TestServiceResponse(false, message);
    }
}
