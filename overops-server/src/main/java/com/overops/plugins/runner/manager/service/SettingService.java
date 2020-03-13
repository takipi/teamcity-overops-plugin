package com.overops.plugins.runner.manager.service;

import com.overops.plugins.runner.manager.model.GeneralSetting;
import com.overops.plugins.runner.manager.model.TestServiceResponse;
import jetbrains.buildServer.serverSide.SProject;

public interface SettingService {
    SProject getProject(String projectId);
    SProject getProjectByExternalId(String projectId);
    GeneralSetting getSetting(String projectId);
    GeneralSetting updateSetting(GeneralSetting setting, String projectId);

    TestServiceResponse testConnection(GeneralSetting setting);
}
