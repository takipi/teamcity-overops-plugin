package com.overops.plugins.runner.manager.extension;

import com.overops.plugins.Constants;
import com.overops.plugins.Util;
import com.overops.plugins.model.OverOpsReportModel;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import jetbrains.buildServer.web.reportTabs.ReportTabUtil;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuildResultTab extends ViewLogTab {

    protected BuildResultTab(@NotNull PagePlaces pagePlaces, @NotNull SBuildServer server, @NotNull final PluginDescriptor pluginDescriptor) {
        super("OverOps Quality Report", "overops", pagePlaces, server);
        setIncludeUrl(pluginDescriptor.getPluginResourcesPath("resultTab.jsp"));
    }

    @Override
    protected void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request, @NotNull SBuild build) {
        BuildArtifact artifact = ReportTabUtil.getArtifact(build, Constants.RUNNER_DISPLAY_NAME + "/" + Constants.OV_REPORTS_FILE_RESULT);
        Optional.ofNullable(artifact).ifPresent(a -> {
            try {
                String result = new BufferedReader(new InputStreamReader(artifact.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
                OverOpsReportModel report = Util.stringToObject(result, OverOpsReportModel.class);
                report.updateSummaryTable();

                model.put("result", report);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });

    }

    @Override
    protected boolean isAvailable(@NotNull HttpServletRequest request, @NotNull SBuild build) {
        return super.isAvailable(request, build) && ReportTabUtil.isAvailable(build, Constants.RUNNER_DISPLAY_NAME + "/" + Constants.OV_REPORTS_FILE_RESULT);
    }
}
