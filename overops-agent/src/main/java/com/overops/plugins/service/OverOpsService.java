package com.overops.plugins.service;

import com.overops.plugins.model.QueryOverOps;
import com.overops.plugins.model.Setting;
import com.overops.plugins.service.impl.ReportBuilder;
import jetbrains.buildServer.agent.BuildProgressLogger;

import java.io.IOException;

public interface OverOpsService {
    ReportBuilder.QualityReport perform(Setting setting, QueryOverOps queryOverOps, BuildProgressLogger logger) throws IOException, InterruptedException;
}
