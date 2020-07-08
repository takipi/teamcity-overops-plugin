package com.overops.plugins.runner;

import com.overops.plugins.Constants;
import com.overops.plugins.service.OverOpsService;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.jetbrains.annotations.NotNull;

public class OverOpsAgentBuildRunner implements AgentBuildRunner {

    @NotNull
    private final OverOpsService overOpsService;
    @NotNull
    private final ArtifactsWatcher artifactsWatcher;

    public OverOpsAgentBuildRunner(@NotNull ArtifactsWatcher artifactsWatcher) {
        this.overOpsService = new OverOpsService();
        this.artifactsWatcher = artifactsWatcher;
    }

    @NotNull
    @Override
    public BuildProcess createBuildProcess(@NotNull AgentRunningBuild runningBuild, @NotNull BuildRunnerContext context) throws RunBuildException {
        return new OverOpsBuildProcess(runningBuild, context, overOpsService, artifactsWatcher);
    }

    @NotNull
    @Override
    public AgentBuildRunnerInfo getRunnerInfo() {
        return new AgentBuildRunnerInfo() {
            @NotNull
            @Override
            public String getType() {
                return Constants.RUNNER_TYPE;
            }

            @Override
            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
                return true;
            }
        };
    }
}
