package com.overops.plugins.model;

import org.springframework.util.StringUtils;

import java.util.Map;

import static com.overops.plugins.Constants.*;

public class Setting {
    private final String appHost;
    private final String environmentID;
    private final String apiKey;

    public Setting(Map<String, String> params) {
        this.appHost = params.get(SETTING_APP_HOST);
        this.environmentID = params.get(SETTING_ENV_ID).toUpperCase();
        this.apiKey = params.get(SETTING_API_TOKEN);
    }

    public String getAppHost() {
        return appHost;
    }

    public String getEnvironmentID() {
        return environmentID;
    }

    public String getAPIKey() {
        return apiKey;
    }

    public void validate() {
        if (StringUtils.isEmpty(appHost)) {
            throw new IllegalArgumentException("Missing host name");
        }

        if (StringUtils.isEmpty(environmentID)) {
            throw new IllegalArgumentException("Missing environment Id");
        }

        if (StringUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("Missing api key");
        }
    }

    @Override
    public String toString() {
        return "GeneralSetting{" +
                "overOpsURL='" + appHost + '\'' +
                ", overOpsSID='" + environmentID + '\'' +
                ", overOpsAPIKey='" + apiKey + '\'' +
                '}';
    }
}
