package com.overops.plugins.runner.manager.extension;

import com.overops.plugins.Constants;
import com.overops.plugins.Result;
import com.overops.plugins.Util;

import java.io.IOException;
import jetbrains.buildServer.controllers.BuildDataExtensionUtil;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildSummaryExtension extends SimplePageExtension {

    private Logger logger = LoggerFactory.getLogger(BuildSummaryExtension.class);

    @NotNull
    private final SBuildServer server;

    public BuildSummaryExtension(@NotNull PagePlaces pagePlaces,
                                 @NotNull PluginDescriptor descriptor,
                                 @NotNull final SBuildServer server) {
        super(pagePlaces, PlaceId.BUILD_SUMMARY, "changeViewers", descriptor.getPluginResourcesPath("buildSummary.jsp"));
        this.server = server;
        register();
    }

    @Override
    public boolean isAvailable(@NotNull HttpServletRequest request) {
        return super.isAvailable(request);
    }

    @Override
    public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
        SBuild build = BuildDataExtensionUtil.retrieveBuild(request, server);
        Optional.ofNullable(build).map(b -> b.getArtifacts(BuildArtifactsViewMode.VIEW_ALL))
            .map(a -> a.getArtifact(Constants.RUNNER_DISPLAY_NAME + "/" + Constants.OV_REPORTS_FILE))
            .ifPresent(artifact -> {
                try {
                    Util.streamToObject(artifact.getInputStream(), Result.class).ifPresent(result -> {
                        model.put("unstable", result.isUnstable());
                    });
                    model.put("overOpsMsg", "There is no report for this build");
                } catch (IOException e) {
                    logger.error("Cannot read the url.");
                }
            });
    }
}
