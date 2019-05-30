package com.overops.plugins;

public class Result {
    private boolean report;
    private boolean unstable;

    public Result() {
    }

    public Result(boolean report, boolean unstable) {
        this.report = report;
        this.unstable = unstable;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
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
            "report=" + report +
            ", unstable=" + unstable +
            '}';
    }
}
