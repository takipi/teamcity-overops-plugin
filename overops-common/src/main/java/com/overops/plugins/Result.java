package com.overops.plugins;

public class Result {
    private boolean unstable;

    public Result() {
    }

    public Result(boolean unstable) {
        this.unstable = unstable;
    }

    public boolean isUnstable() {
        return unstable;
    }

    public void setUnstable(boolean unstable) {
        this.unstable = unstable;
    }

    @Override
    public String toString() {
        return "Result{" +
            "unstable=" + unstable +
            '}';
    }
}
