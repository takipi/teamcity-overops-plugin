package com.overops.plugins.runner.manager.service.impl;

import com.overops.plugins.runner.manager.model.GeneralSetting;
import com.overops.plugins.runner.manager.model.TestServiceResponse;
import com.overops.plugins.runner.manager.service.SettingService;
import com.takipi.api.client.ApiClient;
import com.takipi.api.client.RemoteApiClient;
import com.takipi.api.client.util.client.ClientUtil;
import com.takipi.api.core.url.UrlClient;
import jetbrains.buildServer.serverSide.ParametersDescriptor;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SProjectFeatureDescriptor;
import jetbrains.buildServer.serverSide.settings.ProjectSettings;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsFactory;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

import static com.overops.plugins.Constants.OVER_OPS_MANAGER_KEY;

public class SettingServiceImpl implements SettingService, ProjectSettingsFactory {

    @NotNull
    private final ProjectManager projectManager;

    @NotNull
    private final ProjectSettingsManager projectSettingsManager;

    public SettingServiceImpl(@NotNull ProjectManager projectManager, @NotNull ProjectSettingsManager projectSettingsManager) {
        this.projectManager = projectManager;
        this.projectSettingsManager = projectSettingsManager;
        this.projectSettingsManager.registerSettingsFactory(OVER_OPS_MANAGER_KEY, this);
    }

    @Override
    public SProject getProject(String projectId) {
        return projectManager.findProjectById(projectId);
    }

    @Override
    public SProject getProjectByExternalId(String projectId) {
        return projectManager.findProjectByExternalId(projectId);
    }

    @Override
    public GeneralSetting getSetting(String projectId) {
        SProject project = Optional.ofNullable(getProject(projectId)).orElse(getProjectByExternalId(projectId));
        return project
                .getOwnFeaturesOfType(OVER_OPS_MANAGER_KEY).stream()
                .map(ParametersDescriptor::getParameters)
                .findFirst().map(GeneralSetting::new).orElse(new GeneralSetting());
    }

    @Override
    public GeneralSetting updateSetting(GeneralSetting setting, String projectId) {
        SProject project = Optional.ofNullable(getProject(projectId)).orElse(getProjectByExternalId(projectId));
        SProjectFeatureDescriptor features = project.getOwnFeaturesOfType(OVER_OPS_MANAGER_KEY).stream().findFirst().orElse(null);
        if (Objects.nonNull(features)) {
            project.updateFeature(features.getId(), OVER_OPS_MANAGER_KEY, setting.toMap());
        } else {
            project.addFeature(OVER_OPS_MANAGER_KEY, setting.toMap());
        }
        return setting;
    }

    @Override
    public TestServiceResponse testConnection(GeneralSetting setting) {
        try {
            RemoteApiClient apiClient = (RemoteApiClient) RemoteApiClient.newBuilder().setHostname(setting.getUrl()).setApiKey(setting.getToken()).build();
            UrlClient.Response<String> response = apiClient.testConnection();

            boolean isBadResponse = (response == null) || (response.isBadResponse());
            if (isBadResponse) {
                int code = response != null ? response.responseCode : -1;
                return TestServiceResponse.fail("Unable to connect to API server. Code: " + code);
            }

            if ((setting.getEnvId() != null) && !hasAccessToEnvironment(apiClient, setting.getEnvId())) {
                return TestServiceResponse.fail("API key has no access to environment " + setting.getEnvId());
            }

            return TestServiceResponse.ok("Connection Successful.");
        } catch (Exception e) {
            return TestServiceResponse.fail("REST API error : " + e.getMessage());
        }
    }

    @NotNull
    @Override
    public ProjectSettings createProjectSettings(String s) {
        return new GeneralSetting();
    }


    private boolean hasAccessToEnvironment(ApiClient apiClient, String environmentId) {
        try {
            return ClientUtil.getEnvironments(apiClient)
                    .stream()
                    .anyMatch(summarizedService -> summarizedService.id.equals(environmentId));
        } catch (Exception e) {
            return false;
        }
    }
}
