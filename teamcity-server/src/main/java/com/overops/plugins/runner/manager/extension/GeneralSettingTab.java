package com.overops.plugins.runner.manager.extension;

import com.overops.plugins.runner.manager.model.GeneralSetting;
import com.overops.plugins.runner.manager.service.SettingService;
import com.overops.plugins.runner.manager.service.impl.SettingServiceImpl;
import jetbrains.buildServer.controllers.admin.projects.EditProjectTab;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.overops.plugins.Constants.*;

public class GeneralSettingTab extends EditProjectTab {

    @NotNull
    private final SettingService settingService;

    public GeneralSettingTab(@NotNull PagePlaces pagePlaces,
                             @NotNull PluginDescriptor descriptor,
                             @NotNull SettingServiceImpl settingService) {
        super(pagePlaces, "GeneralSettingTab",  descriptor.getPluginResourcesPath("generalSetting.jsp"), GENERAL_SETTING_TAB_TITLE);
        this.settingService = settingService;
        addCssFile(descriptor.getPluginResourcesPath("css/overops.css"));
//        addJsFile(pluginDescriptor.getPluginResourcesPath("manageSonarServers.js"));
    }

    @Override
    public void fillModel(@NotNull final Map<String, Object> model, @NotNull final HttpServletRequest request) {
        Optional.ofNullable(getProject(request)).ifPresent(project -> {
            GeneralSetting setting = settingService.getSetting(project.getExternalId());
            model.put(FIELD_PROJECT_ID, project.getExternalId());
            model.put(SETTING_URL, setting.getUrl());
            model.put(SETTING_ENV_ID, setting.getEnvId());
            model.put(SETTING_TOKEN, setting.getToken());
        });

    }
}
