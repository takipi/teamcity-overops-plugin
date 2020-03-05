package com.overops.plugins.runner.manager.controller;

import com.overops.plugins.Constants;
import com.overops.plugins.runner.manager.service.SettingService;
import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.StatefulObject;
import jetbrains.buildServer.controllers.admin.projects.BuildTypeForm;
import jetbrains.buildServer.controllers.admin.projects.EditRunTypeControllerExtension;
import jetbrains.buildServer.serverSide.BuildTypeSettings;
import jetbrains.buildServer.serverSide.SBuildServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static com.overops.plugins.Constants.*;

public class EditSettingControllerExtension implements EditRunTypeControllerExtension {

    @NotNull
    private final SettingService settingService;

    public EditSettingControllerExtension(@NotNull SettingService settingService, @NotNull final SBuildServer server) {
        server.registerExtension(EditRunTypeControllerExtension.class, Constants.RUNNER_TYPE, this);
        this.settingService = settingService;

    }

    @Override
    public void fillModel(@NotNull HttpServletRequest request, @NotNull BuildTypeForm form, @NotNull Map<String, Object> model) {
        Optional.ofNullable(settingService.getSetting(form.getProject().getProjectId()))
                .ifPresent(setting -> {
                    model.put(SETTING_APP_HOST, setting.getUrl());
                    model.put(SETTING_ENV_ID, setting.getEnvId());
                    model.put(SETTING_API_TOKEN, setting.getToken());
                });
    }

    @Override
    public void updateState(@NotNull HttpServletRequest request, @NotNull BuildTypeForm form) {

    }

    @Nullable
    @Override
    public StatefulObject getState(@NotNull HttpServletRequest request, @NotNull BuildTypeForm form) {
        return null;
    }

    @NotNull
    @Override
    public ActionErrors validate(@NotNull HttpServletRequest request, @NotNull BuildTypeForm form) {
        return new ActionErrors();
    }

    @Override
    public void updateBuildType(@NotNull HttpServletRequest request, @NotNull BuildTypeForm form, @NotNull BuildTypeSettings buildTypeSettings, @NotNull ActionErrors errors) {

    }
}
