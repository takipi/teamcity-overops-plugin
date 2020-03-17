package com.overops.plugins.service.impl;

import com.overops.plugins.model.OverOpsConfiguration;
import com.overops.plugins.model.QualityReport;
import com.overops.plugins.service.OverOpsService;
import com.takipi.api.client.RemoteApiClient;
import com.takipi.api.client.data.view.SummarizedView;
import com.takipi.api.client.util.regression.RegressionInput;
import com.takipi.api.client.util.view.ViewUtil;
import jetbrains.buildServer.agent.BuildProgressLogger;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.overops.plugins.Util.convertToMinutes;

public class OverOpsServiceImpl implements OverOpsService {
    private static final String SEPARATOR = ",";

    private OverOpsConfiguration config;
    private PrintStream printStream;

    @Override
    public QualityReport produceReport(OverOpsConfiguration config, BuildProgressLogger logger) {
        initProcessVariables(config, logger);
        printlnMessage("Build Step: Starting OverOps Quality Gate....");
        empiricalWaitFor3SecondsToEnsureAllDataIsInOverOps();

        config.validate(printStream);

        RemoteApiClient apiClient = createRemoteApiClient();
        SummarizedView allEventsView = retrieveAllEventsView(apiClient);
        RegressionInput input = setupRegressionData(allEventsView.id);

        return new ReportBuilder(apiClient, input, config, printStream).build();
    }

    @NotNull
    private SummarizedView retrieveAllEventsView(RemoteApiClient apiClient) {
        SummarizedView allEventsView = ViewUtil.getServiceViewByName(apiClient, config.getEnvironmentID(), "All Events");

        if (Objects.isNull(allEventsView)) {
            printlnMessage("Could not acquire ID for 'All Events'. Please check connection to " + config.getAppHost());
            throw new IllegalStateException(
                    "Could not acquire ID for 'All Events'. Please check connection to " + config.getAppHost());
        }

        return allEventsView;
    }

    private void initProcessVariables(OverOpsConfiguration config, BuildProgressLogger logger) {
        this.config = config;
        initPrintStream(logger);
    }

    private void initPrintStream(BuildProgressLogger logger) {
        printStream = config.isDebug() ? new TeamCityPrintWriter(System.out, logger) : null;
    }

    private RemoteApiClient createRemoteApiClient() {
        RemoteApiClient apiClient = (RemoteApiClient) RemoteApiClient.newBuilder()
                .setHostname(config.getAppHost())
                .setApiKey(config.getAPIKey())
                .build();

        if (config.isDebug()) {
            apiClient.addObserver((operation, url, request, response, responseCode, time) -> {
                String message = new StringBuilder()
                        .append(operation).append(" took ").append(time / 1000).append("ms for ").append(url)
                        .append(". Response: ").append(response).toString();

                printlnMessage(message);
            });
        }
        return apiClient;
    }

    private void empiricalWaitFor3SecondsToEnsureAllDataIsInOverOps() {
        try {
            Thread.sleep(30000);
        } catch (Exception e) {
            printlnMessage("Can not hold the process.");
        }
    }

    private void printlnMessage(String message) {
        if (Objects.nonNull(printStream)) {
            printStream.println(message);
        }
    }

    private RegressionInput setupRegressionData(String allEventsViewId) {

        RegressionInput input = new RegressionInput();
        input.serviceId = config.getEnvironmentID();
        input.viewId = allEventsViewId;
        input.applictations = parseArrayString(config.getApplicationName());
        input.deployments = parseArrayString(config.getDeploymentName());
        input.criticalExceptionTypes = parseArrayString(config.getCriticalExceptionTypes());

        if (config.isRegressionPresent()) {
            input.activeTimespan = convertToMinutes(config.getActiveTimespan());
            input.baselineTime = config.getBaselineTimespan();
            input.baselineTimespan = convertToMinutes(config.getBaselineTimespan());
            input.minVolumeThreshold = config.getMinVolumeThreshold();
            input.minErrorRateThreshold = config.getMinErrorRateThreshold();
            input.regressionDelta = config.getRegressionDelta();
            input.criticalRegressionDelta = config.getCriticalRegressionDelta();
            input.applySeasonality = config.isApplySeasonality();
            input.validate();
        }

        printInputs(input);

        return input;
    }

    private Collection<String> parseArrayString(String value) {
        return StringUtils.isEmpty(value) ? Collections.emptySet()
                : Arrays.asList(value.trim().split(Pattern.quote(SEPARATOR)));
    }

    private void printInputs(RegressionInput input) {
        if (Objects.nonNull(printStream)) {
            printStream.println(input);

            printStream.println("Max unique errors  = " + config.getMaxUniqueErrors());
            printStream.println("Max error volume  = " + config.getMaxErrorVolume());
            printStream.println("Check new errors  = " + config.isNewEvents());
            printStream.println("Check resurfaced errors  = " + config.isResurfacedErrors());

            String regexFilter = config.getRegexFilter() == null ? "" : config.getRegexFilter();
            printStream.println("Regex filter  = " + regexFilter);
        }
    }
}
