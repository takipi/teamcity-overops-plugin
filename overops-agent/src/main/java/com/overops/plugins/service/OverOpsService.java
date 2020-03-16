package com.overops.plugins.service;

import com.overops.plugins.model.OverOpsConfiguration;
import com.overops.plugins.model.QualityReport;
import jetbrains.buildServer.agent.BuildProgressLogger;

import java.io.IOException;

public interface OverOpsService {
    QualityReport produceReport(OverOpsConfiguration overOpsConfiguration, BuildProgressLogger logger) throws IOException, InterruptedException;
}
