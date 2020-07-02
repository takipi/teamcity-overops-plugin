package com.overops.plugins.runner;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.overops.plugins.service.OverOpsService;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.jetbrains.annotations.NotNull;

public class OverOpsBuildProcess implements BuildProcess {

    @NotNull
    private BuildProgressLogger logger;

    private OverOpsProcess overOpsProcess;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private Future<BuildFinishedStatus> processFuture;

    public OverOpsBuildProcess(@NotNull AgentRunningBuild runningBuild, @NotNull BuildRunnerContext context,
        @NotNull OverOpsService overOpsService, @NotNull ArtifactsWatcher artifactsWatcher) {
        this.logger = runningBuild.getBuildLogger();
        this.overOpsProcess = new OverOpsProcess(runningBuild, logger, artifactsWatcher, context, overOpsService);
    }

    @Override
    public void start() throws RunBuildException {
        logger.message("OverOps agent started:");
        processFuture = executor.submit(overOpsProcess);
    }

    @Override
    public boolean isFinished() {
        return processFuture.isDone();
    }

    @Override
    public boolean isInterrupted() {
        return processFuture.isCancelled() && isFinished();
    }

    @Override
    public void interrupt() {
        logger.message("Interrupt OverOps build step");
        if (processFuture != null) {
            processFuture.cancel(true);
        }
    }

    @Override
    public BuildFinishedStatus waitFor() throws RunBuildException {
        try {
            return processFuture.get();
        } catch (final InterruptedException | CancellationException e) {
            return BuildFinishedStatus.INTERRUPTED;
        } catch (final ExecutionException | IllegalStateException e) {
            return BuildFinishedStatus.FINISHED_FAILED;
        } finally {
            executor.shutdown();
        }
    }

}
