package com.overops.plugins.runner;

import static com.overops.plugins.Constants.OV_REPORTS_FILE;
import static com.overops.plugins.Constants.RUNNER_DISPLAY_NAME;
import static com.overops.plugins.Constants.SETTING_ENV_ID;
import static com.overops.plugins.Constants.SETTING_TOKEN;
import static com.overops.plugins.Constants.SETTING_URL;

import com.overops.plugins.Result;
import com.overops.plugins.Util;
import com.overops.plugins.model.QueryOverOps;
import com.overops.plugins.model.Setting;
import com.overops.plugins.service.OverOpsService;
import com.overops.plugins.service.impl.ReportBuilder;
import com.takipi.api.client.util.cicd.OOReportEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

public class OverOpsProcess implements Callable<BuildFinishedStatus> {

    @NotNull
    private final AgentRunningBuild agentRunningBuild;
    @NotNull
    private final BuildRunnerContext context;
    @NotNull
    private final BuildProgressLogger logger;
    @NotNull
    private ArtifactsWatcher artifactsWatcher;
    @NotNull
    private final OverOpsService overOpsService;

    public OverOpsProcess(@NotNull AgentRunningBuild agentRunningBuild, @NotNull BuildProgressLogger logger,
        @NotNull ArtifactsWatcher artifactsWatcher, @NotNull BuildRunnerContext context,
        @NotNull OverOpsService overOpsService) {
        this.agentRunningBuild = agentRunningBuild;
        this.logger = logger;
        this.artifactsWatcher = artifactsWatcher;
        this.context = context;
        this.overOpsService = overOpsService;
    }

    @Override
    public BuildFinishedStatus call() throws Exception {

        Setting setting = new Setting(context.getRunnerParameters().get(SETTING_URL),
            context.getRunnerParameters().get(SETTING_ENV_ID), context.getRunnerParameters().get(SETTING_TOKEN));

        QueryOverOps params = QueryOverOps.mapToObject(context.getRunnerParameters());
        try {
            ReportBuilder.QualityReport report = overOpsService.perform(setting, params);
            publishArtifacts(report);
            report.getCriticalErrors().forEach(e -> logger.error(e.toString()));
            report.getTopErrors().forEach(e -> logger.error(e.toString()));
            report.getResurfacedErrors().forEach(e -> logger.error(e.toString()));
            if (report.isMarkedUnstable() || report.getUnstable()) {
                logger.error("Build is unstable");
                return BuildFinishedStatus.FINISHED_WITH_PROBLEMS;
            }
        } catch (InterruptedException  | IllegalStateException | IOException e) {
            logger.error("Failed to start OverOps test: " + e.getMessage());
            return BuildFinishedStatus.INTERRUPTED;
        } catch (Exception e) {
            logger.error("Caught exception: " + e.getMessage());
            return BuildFinishedStatus.FINISHED_FAILED;
        }
        return BuildFinishedStatus.FINISHED_SUCCESS;
    }

    private void publishArtifacts(ReportBuilder.QualityReport report) {
        File buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" +
            agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" +
            agentRunningBuild.getBuildNumber() + "/" + RUNNER_DISPLAY_NAME);
        File file = new File(buildDirectory, OV_REPORTS_FILE);
        try {
            FileUtils.touch(file);
            Result result = new Result(false, report.isMarkedUnstable() || report.getUnstable());
            Util.objectToString(result).ifPresent(o -> appendStringToFile(file, o));
        } catch (IOException e) {
            logger.error("Cannot create artifact: " + e.getMessage());
        }
        artifactsWatcher.addNewArtifactsPath(file + "=>" + RUNNER_DISPLAY_NAME);
    }

    private void appendStringToFile(File file, String content) {
        try {
            Files.write(Paths.get(file.toURI()), content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Cannot write line into artifact: " + e.getMessage());
        }
    }
}
