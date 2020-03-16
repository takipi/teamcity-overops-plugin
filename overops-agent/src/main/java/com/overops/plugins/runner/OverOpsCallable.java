package com.overops.plugins.runner;

import com.overops.plugins.Result;
import com.overops.plugins.Util;
import com.overops.plugins.model.OverOpsReportModel;
import com.overops.plugins.model.OverOpsConfiguration;
import com.overops.plugins.model.QualityReport;
import com.overops.plugins.service.OverOpsService;
import com.overops.plugins.utils.ReportUtils;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;

import static com.overops.plugins.Constants.*;

public class OverOpsCallable implements Callable<BuildFinishedStatus> {

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

    public OverOpsCallable(@NotNull AgentRunningBuild agentRunningBuild, @NotNull BuildProgressLogger logger,
                           @NotNull ArtifactsWatcher artifactsWatcher, @NotNull BuildRunnerContext context,
                           @NotNull OverOpsService overOpsService) {
        this.agentRunningBuild = agentRunningBuild;
        this.logger = logger;
        this.artifactsWatcher = artifactsWatcher;
        this.context = context;
        this.overOpsService = overOpsService;
    }

    @Override
    public BuildFinishedStatus call() {
        OverOpsConfiguration configuration = new OverOpsConfiguration(context.getRunnerParameters());

        OverOpsReportModel reportModel = generateOverOpsReportModel(configuration);
        return publishModelAndReturnStatus(configuration, reportModel);
    }

    @NotNull
    private BuildFinishedStatus publishModelAndReturnStatus(OverOpsConfiguration configuration, OverOpsReportModel reportModel) {
        publishArtifactObject(reportModel, OV_REPORTS_FILE_RESULT, "Cannot create result artifact: ");

        logger.message(reportModel.getSummary());

        if (reportModel.getException() != null) {
            return configuration.isErrorSuccess() ? BuildFinishedStatus.FINISHED_SUCCESS : BuildFinishedStatus.INTERRUPTED;
        } else {
            publishArtifactObject(new Result(reportModel.isUnstable()), OV_REPORTS_FILE, "Cannot create artifact: ");
            if (reportModel.isUnstable() && reportModel.isMarkedUnstable()) {
                return BuildFinishedStatus.FINISHED_FAILED;
            }
        }

        return BuildFinishedStatus.FINISHED_SUCCESS;
    }

    @NotNull
    private OverOpsReportModel generateOverOpsReportModel(OverOpsConfiguration configuration) {
        OverOpsReportModel reportModel;

        try {
            QualityReport report = overOpsService.produceReport(configuration, logger);
            reportModel = ReportUtils.copyResult(report);
        } catch (Exception exception) {
            reportModel = ReportUtils.exceptionResult(exception);
            logger.error("OverOps encountered an exception");
        }
        return reportModel;
    }

    private void publishArtifactObject(Object object, String fileName, String ioErrorMessage) {
        File file = getFileInBuildDirectory(fileName);
        try {
            FileUtils.touch(file);
            Util.objectToString(object).ifPresent(o -> appendStringToFile(file, o));
        } catch (IOException e) {
            logger.error(ioErrorMessage + e.getMessage());
        }
        artifactsWatcher.addNewArtifactsPath(file + "=>" + RUNNER_DISPLAY_NAME);
    }

    @NotNull
    private File getBuildDirectory() {
        return new File(new StringBuilder().append(agentRunningBuild.getBuildTempDirectory()).append("/")
                .append(agentRunningBuild.getProjectName()).append("/")
                .append(agentRunningBuild.getBuildTypeName()).append("/")
                .append(agentRunningBuild.getBuildNumber()).append("/")
                .append(RUNNER_DISPLAY_NAME).toString());
    }

    private File getFileInBuildDirectory(String fileName) {
        return new File(getBuildDirectory(), fileName);
    }

    private void appendStringToFile(File file, String content) {
        try {
            Files.write(Paths.get(file.toURI()), content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Cannot write line into artifact: " + e.getMessage());
        }
    }

}
