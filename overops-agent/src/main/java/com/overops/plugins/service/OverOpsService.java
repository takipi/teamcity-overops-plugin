package com.overops.plugins.service;

import com.overops.plugins.model.OverOpsConfiguration;
import com.overops.plugins.service.impl.ReportBuilder;
import jetbrains.buildServer.agent.BuildProgressLogger;

import java.io.IOException;

public interface OverOpsService {
    ReportBuilder.QualityReport perform(OverOpsConfiguration overOpsConfiguration, BuildProgressLogger logger) throws IOException, InterruptedException;
}
