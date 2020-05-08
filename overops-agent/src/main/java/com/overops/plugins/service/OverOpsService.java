package com.overops.plugins.service;

import com.overops.report.service.model.QualityReport;

import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;

import java.io.IOException;

public interface OverOpsService {
    QualityReport perform(BuildRunnerContext context, BuildProgressLogger logger) throws IOException, InterruptedException;
}
