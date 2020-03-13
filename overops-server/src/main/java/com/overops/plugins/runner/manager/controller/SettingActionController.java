package com.overops.plugins.runner.manager.controller;

import com.overops.plugins.runner.manager.model.GeneralSetting;
import com.overops.plugins.runner.manager.model.TestServiceResponse;
import com.overops.plugins.runner.manager.service.SettingService;
import com.overops.plugins.runner.manager.service.impl.SettingServiceImpl;
import jetbrains.buildServer.controllers.BaseAjaxActionController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.ControllerAction;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;
import java.util.Optional;

import static com.overops.plugins.Constants.*;

public class SettingActionController extends BaseAjaxActionController implements ControllerAction {

    @NotNull
    private final SettingService settingService;

    public SettingActionController(@NotNull SBuildServer server,
                                   @NotNull WebControllerManager controllerManager,
                                   @NotNull SettingServiceImpl settingService) {
        super(controllerManager);
        this.settingService = settingService;
        controllerManager.registerController("/admin/manageOverOps.html", this);
        registerAction(this);
    }

    @Override
    public boolean canProcess(@NotNull HttpServletRequest httpServletRequest) {
        //TODO here will be authority check
        return true;
    }

    @Override
    public void process(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable Element ajaxResponse) {
        String url = request.getParameter(FIELD_URL);
        String token = request.getParameter(FIELD_TOKEN);
        String envId = request.getParameter(FIELD_ENV_ID);
        if (Boolean.parseBoolean(request.getParameter(FIELD_TESTING))) {
            TestServiceResponse test = settingService.testConnection(new GeneralSetting(url, envId, token));
            if (Objects.nonNull(ajaxResponse)) {
                ajaxResponse.setAttribute("status", test.isStatus() ? "OK" : "FAIL");
                ajaxResponse.setAttribute("message", test.getMessage());
            }
        } else {
            Optional.ofNullable(settingService.getProjectByExternalId(request.getParameter(FIELD_PROJECT_ID)))
                    .filter(project -> Objects.nonNull(ajaxResponse))
                    .ifPresent(project -> {
                GeneralSetting setting = settingService.getSetting(request.getParameter(FIELD_PROJECT_ID));
                setting.setUrl(url);
                setting.setToken(token);
                setting.setEnvId(envId);
                setting = settingService.updateSetting(setting, request.getParameter(FIELD_PROJECT_ID));
                if (Objects.isNull(setting)) {
                    ajaxResponse.setAttribute("status", "FAIL");
                } else {
                    ajaxResponse.setAttribute("status", "OK");
                }
                project.persist();
            });
        }
    }
}
