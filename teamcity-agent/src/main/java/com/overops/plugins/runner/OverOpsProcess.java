package com.overops.plugins.runner;

import com.overops.plugins.Result;
import com.overops.plugins.Util;
import com.overops.plugins.model.OverOpsReportModel;
import com.overops.plugins.model.QueryOverOps;
import com.overops.plugins.model.Setting;
import com.overops.plugins.service.OverOpsService;
import com.overops.plugins.service.impl.ReportBuilder;
import com.overops.plugins.utils.ReportUtils;
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
import jetbrains.buildServer.messages.Status;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import static com.overops.plugins.Constants.*;

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
        params.setServiceId(setting.getOverOpsSID());
        try {
            ReportBuilder.QualityReport report = overOpsService.perform(setting, params, logger);
            boolean unstable = report.isMarkedUnstable() && report.getUnstable();
            String summary = getSummary(report);
            publishArtifacts(unstable);
            publishReportArtifact(ReportUtils.copyResult(report));

            if (unstable) {
                logger.message(summary, Status.FAILURE);
                return BuildFinishedStatus.FINISHED_WITH_PROBLEMS;
            }
            logger.message(summary, Status.NORMAL);
        } catch (InterruptedException  | IllegalStateException | IOException e) {
            logger.error("Failed to start OverOps test: " + e.getMessage());
            return BuildFinishedStatus.INTERRUPTED;
        } catch (Exception e) {
            logger.error("Caught exception: " + e.getMessage());
            return BuildFinishedStatus.FINISHED_FAILED;
        }
        return BuildFinishedStatus.FINISHED_SUCCESS;
    }

    private void publishArtifacts(Boolean markUnstable) {
        File buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" +
            agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" +
            agentRunningBuild.getBuildNumber() + "/" + RUNNER_DISPLAY_NAME);
        File file = new File(buildDirectory, OV_REPORTS_FILE);
        try {
            FileUtils.touch(file);
            Result result = new Result(false, markUnstable);
            Util.objectToString(result).ifPresent(o -> appendStringToFile(file, o));
        } catch (IOException e) {
            logger.error("Cannot create artifact: " + e.getMessage());
        }
        artifactsWatcher.addNewArtifactsPath(file + "=>" + RUNNER_DISPLAY_NAME);
    }

    private void publishReportArtifact(OverOpsReportModel report) {
        File buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" +
                agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" +
                agentRunningBuild.getBuildNumber() + "/" + RUNNER_DISPLAY_NAME);
        File file = new File(buildDirectory, OV_REPORTS_FILE_RESULT);
        try {
            FileUtils.touch(file);
            Util.objectToString(report).ifPresent(o -> appendStringToFile(file, o));
        } catch (IOException e) {
            logger.error("Cannot create result artifact: " + e.getMessage());
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

    private String getSummary(ReportBuilder.QualityReport report) {
        if (report.getUnstable() && report.isMarkedUnstable()) {
            //the build is unstable when marking the build as unstable
            return "OverOps has marked build "+ report.getDeploymentName() + "  as unstable because the below quality gate(s) were not met.";
        } else if (!report.isMarkedUnstable() && report.getUnstable()) {
            //unstable build stable when NOT marking the build as unstable
            return "OverOps has detected issues with build "+ report.getDeploymentName() + "  but did not mark the build as unstable.";
        } else {
            //stable build when marking the build as unstable
            return "Congratulations, build " + report.getDeploymentName() + " has passed all quality gates!";
        }
    }


}
