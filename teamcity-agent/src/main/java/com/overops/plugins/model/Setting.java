package com.overops.plugins.model;

public class Setting {
    private String overOpsURL;
    private String overOpsSID;
    private String overOpsAPIKey;

    public Setting(String overOpsURL, String overOpsSID, String overOpsAPIKey) {
        this.overOpsURL = overOpsURL;
        this.overOpsSID = overOpsSID;
        this.overOpsAPIKey = overOpsAPIKey;
    }

    public String getOverOpsURL() {
        return overOpsURL;
    }

    public void setOverOpsURL(String overOpsURL) {
        this.overOpsURL = overOpsURL;
    }

    public String getOverOpsSID() {
        return overOpsSID;
    }

    public void setOverOpsSID(String overOpsSID) {
        this.overOpsSID = overOpsSID;
    }

    public String getOverOpsAPIKey() {
        return overOpsAPIKey;
    }

    public void setOverOpsAPIKey(String overOpsAPIKey) {
        this.overOpsAPIKey = overOpsAPIKey;
    }

    @Override
    public String toString() {
        return "GeneralSetting{" +
                "overOpsURL='" + overOpsURL + '\'' +
                ", overOpsSID='" + overOpsSID + '\'' +
                ", overOpsAPIKey='" + overOpsAPIKey + '\'' +
                '}';
    }
}
