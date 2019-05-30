package com.overops.plugins;

import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import org.jetbrains.annotations.NotNull;

public class OverOpsReportPlugin extends AgentLifeCycleAdapter {
    @Override
    public void buildStarted(@NotNull AgentRunningBuild runningBuild) {
       super.buildStarted(runningBuild);
    }

    @Override
    public void beforeBuildFinish(@NotNull AgentRunningBuild build, @NotNull BuildFinishedStatus buildStatus) {
       super.beforeBuildFinish(build, buildStatus);
    }
}
