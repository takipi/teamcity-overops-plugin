package com.overops.plugins.service.impl;

import com.overops.plugins.model.OverOpsConfiguration;
import com.overops.plugins.service.OverOpsService;
import com.takipi.api.client.RemoteApiClient;
import com.takipi.api.client.data.view.SummarizedView;
import com.takipi.api.client.observe.Observer;
import com.takipi.api.client.util.regression.RegressionInput;
import com.takipi.api.client.util.view.ViewUtil;
import jetbrains.buildServer.agent.BuildProgressLogger;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.overops.plugins.Util.convertToMinutes;

public class OverOpsServiceImpl implements OverOpsService {
    private static final String SEPARATOR = ",";
    private boolean runRegressions = false;

    @Override
    public ReportBuilder.QualityReport perform(OverOpsConfiguration overOpsConfiguration, BuildProgressLogger logger) throws IOException, InterruptedException {
        PrintStream printStream;
        if (convertToMinutes(overOpsConfiguration.getBaselineTimespan()) > 0) {
            runRegressions = true;
        }
        if (overOpsConfiguration.isDebug()) {
            printStream = new TeamCityPrintWriter(System.out, logger);
        } else {
            printStream = null;
        }

        pauseForTheCause(printStream);

        overOpsConfiguration.validate(printStream);

        RemoteApiClient apiClient = (RemoteApiClient) RemoteApiClient.newBuilder().setHostname(overOpsConfiguration.getAppHost()).setApiKey(overOpsConfiguration.getAPIKey()).build();

        if (Objects.nonNull(printStream) && (overOpsConfiguration.isDebug())) {
            apiClient.addObserver(new ApiClientObserver(printStream, overOpsConfiguration.isDebug()));
        }

        SummarizedView allEventsView = ViewUtil.getServiceViewByName(apiClient, overOpsConfiguration.getEnvironmentID(), "All Events");

        if (Objects.isNull(allEventsView)) {
            if (Objects.nonNull(printStream)) {
                printStream.println("Could not acquire ID for 'All Events'. Please check connection to " + overOpsConfiguration.getAppHost());
            }
            throw new IllegalStateException(
                    "Could not acquire ID for 'All Events'. Please check connection to " + overOpsConfiguration.getAppHost());
        }

        RegressionInput input = setupRegressionData(overOpsConfiguration, allEventsView, printStream);
        return ReportBuilder.execute(apiClient, input, overOpsConfiguration.getMaxErrorVolume(), overOpsConfiguration.getMaxUniqueErrors(),
                overOpsConfiguration.getPrintTopIssues(), overOpsConfiguration.getRegexFilter(), overOpsConfiguration.isNewEvents(), overOpsConfiguration.isResurfacedErrors(),
                runRegressions, overOpsConfiguration.isMarkUnstable(), printStream, overOpsConfiguration.isDebug());

    }

    //sleep to ensure all data is in OverOps
    private static void pauseForTheCause(PrintStream printStream) {
        if (Objects.nonNull(printStream)) {
            printStream.println("Build Step: Starting OverOps Quality Gate....");
        }
        try {
            Thread.sleep(30000);
        } catch (Exception e) {
            if (Objects.nonNull(printStream)) {
                printStream.println("Can not hold the process.");
            }
        }
    }

    private RegressionInput setupRegressionData(OverOpsConfiguration overOpsConfiguration, SummarizedView allEventsView, PrintStream printStream) {

        RegressionInput input = new RegressionInput();
        input.serviceId = overOpsConfiguration.getEnvironmentID();
        input.viewId = allEventsView.id;
        input.applictations = parseArrayString(overOpsConfiguration.getApplicationName(), printStream, "Application Name");
        input.deployments = parseArrayString(overOpsConfiguration.getDeploymentName(), printStream, "Deployment Name");
        input.criticalExceptionTypes = parseArrayString(overOpsConfiguration.getCriticalExceptionTypes(), printStream,
                "Critical Exception Types");

        if (runRegressions) {
            input.activeTimespan = convertToMinutes(overOpsConfiguration.getActiveTimespan());
            input.baselineTime = overOpsConfiguration.getBaselineTimespan();
            input.baselineTimespan = convertToMinutes(overOpsConfiguration.getBaselineTimespan());
            input.minVolumeThreshold = overOpsConfiguration.getMinVolumeThreshold();
            input.minErrorRateThreshold = overOpsConfiguration.getMinErrorRateThreshold();
            input.regressionDelta = overOpsConfiguration.getRegressionDelta();
            input.criticalRegressionDelta = overOpsConfiguration.getCriticalRegressionDelta();
            input.applySeasonality = overOpsConfiguration.isApplySeasonality();
            input.validate();
        }

        printInputs(overOpsConfiguration, printStream, input);

        return input;
    }

    private static Collection<String> parseArrayString(String value, PrintStream printStream, String name) {
        if (StringUtils.isEmpty(value)) {
            return Collections.emptySet();
        }

        return Arrays.asList(value.trim().split(Pattern.quote(SEPARATOR)));
    }

    private void printInputs(OverOpsConfiguration overOpsConfiguration, PrintStream printStream, RegressionInput input) {

        if (Objects.nonNull(printStream)) {
            printStream.println(input);

            printStream.println("Max unique errors  = " + overOpsConfiguration.getMaxUniqueErrors());
            printStream.println("Max error volume  = " + overOpsConfiguration.getMaxErrorVolume());
            printStream.println("Check new errors  = " + overOpsConfiguration.isNewEvents());
            printStream.println("Check resurfaced errors  = " + overOpsConfiguration.isResurfacedErrors());

            String regexPrint;

            if (Objects.nonNull(overOpsConfiguration.getRegexFilter())) {
                regexPrint = overOpsConfiguration.getRegexFilter();
            } else {
                regexPrint = "";
            }

            printStream.println("Regex filter  = " + regexPrint);
        }
    }

    protected static class ApiClientObserver implements Observer {

        private final PrintStream printStream;
        private final boolean verbose;

        public ApiClientObserver(PrintStream printStream, boolean verbose) {
            this.printStream = printStream;
            this.verbose = verbose;
        }

        @Override
        public void observe(Operation operation, String url, String request, String response, int responseCode, long time) {
            StringBuilder output = new StringBuilder();

            output.append(operation);
            output.append(" took ");
            output.append(time / 1000);
            output.append("ms for ");
            output.append(url);

            if (verbose) {
                output.append(". Response: ");
                output.append(response);
            }

            printStream.println(output.toString());
        }
    }
}
