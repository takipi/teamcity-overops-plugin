package com.overops.plugins;

public class Result {
    private final boolean unstable;

    public Result(boolean unstable) {
        this.unstable = unstable;
    }

    public boolean isUnstable() {
        return unstable;
    }

    @Override
    public String toString() {
        return "Result{" +
            "unstable=" + unstable +
            '}';
    }
}
