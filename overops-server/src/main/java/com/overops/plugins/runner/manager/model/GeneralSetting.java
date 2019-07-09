package com.overops.plugins.runner.manager.model;

import com.overops.plugins.Constants;
import jetbrains.buildServer.serverSide.settings.ProjectSettings;
import org.jdom.Element;

import java.util.HashMap;
import java.util.Map;

import static com.overops.plugins.Constants.SETTING_ENV_ID;
import static com.overops.plugins.Constants.SETTING_TOKEN;
import static com.overops.plugins.Constants.SETTING_URL;

public class GeneralSetting implements ProjectSettings {
    private String url;
    private String envId;
    private String token;

    public GeneralSetting() {
    }

    public GeneralSetting(Map<String, String> parameters) {
        this.url = parameters.getOrDefault(SETTING_URL, "");
        this.envId = parameters.getOrDefault(SETTING_ENV_ID, "");
        this.token = parameters.getOrDefault(SETTING_TOKEN, "");
    }

    public GeneralSetting(String url, String envId, String token) {
        this.url = url;
        this.envId = envId;
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnvId() {
        return envId;
    }

    public void setEnvId(String envId) {
        this.envId = envId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void dispose() {
        //do nothing
    }

    @Override
    public void readFrom(Element element) {
        setUrl(element.getAttributeValue(SETTING_URL));
        setEnvId(element.getAttributeValue(Constants.SETTING_ENV_ID));
        setToken(element.getAttributeValue(Constants.SETTING_TOKEN));
    }

    @Override
    public void writeTo(Element element) {
        element.setAttribute(SETTING_URL, this.url);
        element.setAttribute(Constants.SETTING_ENV_ID, this.envId);
        element.setAttribute(Constants.SETTING_TOKEN, this.token);
    }

    public Map<String, String> toMap() {
        final Map<String, String> params = new HashMap<>();
        params.put(SETTING_URL, this.url);
        params.put(SETTING_ENV_ID, this.envId);
        params.put(SETTING_TOKEN, this.token);
        return params;
    }
}
