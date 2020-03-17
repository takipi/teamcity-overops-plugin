package com.overops.plugins.service.impl;

import com.google.gson.Gson;
import com.overops.plugins.model.OOReportRegressedEvent;
import com.overops.plugins.model.OverOpsConfiguration;
import com.overops.plugins.model.QualityReport;
import com.takipi.api.client.ApiClient;
import com.takipi.api.client.result.event.EventResult;
import com.takipi.api.client.util.cicd.OOReportEvent;
import com.takipi.api.client.util.cicd.ProcessQualityGates;
import com.takipi.api.client.util.cicd.QualityGateReport;
import com.takipi.api.client.util.regression.*;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.Pattern;

public class ReportBuilder {

    private final ApiClient apiClient;
    private final RegressionInput input;
    private final OverOpsConfiguration config;
    private final PrintStream output;

    public ReportBuilder(ApiClient apiClient, RegressionInput input, OverOpsConfiguration config, PrintStream output) {
        this.apiClient = apiClient;
        this.input = input;
        this.config = config;
        this.output = output;
    }

    private static boolean allowEvent(EventResult event, Pattern pattern) {
        if (pattern == null) {
            return true;
        }

        String json = new Gson().toJson(event);
        boolean result = !pattern.matcher(json).find();

        return result;
    }

    private static List<EventResult> getSortedEventsByVolume(Collection<EventResult> events) {

        List<EventResult> result = new ArrayList<EventResult>(events);

        result.sort((o1, o2) -> {
            long v1;
            long v2;

            if (o1.stats != null) {
                v1 = o1.stats.hits;
            } else {
                v1 = 0;
            }

            if (o2.stats != null) {
                v2 = o2.stats.hits;
            } else {
                v2 = 0;
            }

            return (int) (v2 - v1);
        });

        return result;
    }

    private static void addEvent(Set<EventResult> events, EventResult event,
                                 Pattern pattern, PrintStream output, boolean verbose) {

        if (allowEvent(event, pattern)) {
            events.add(event);
        } else if ((output != null) && (verbose)) {
            output.println(event + " did not match regexFilter and was skipped");
        }
    }

    private static Collection<EventResult> filterEvents(RateRegression rateRegression,
                                                        Pattern pattern, PrintStream output, boolean verbose) {

        Set<EventResult> result = new HashSet<>();

        if (pattern != null) {

            for (EventResult event : rateRegression.getNonRegressions()) {
                addEvent(result, event, pattern, output, verbose);
            }

            for (EventResult event : rateRegression.getAllNewEvents().values()) {
                addEvent(result, event, pattern, output, verbose);
            }

            for (RegressionResult regressionResult : rateRegression.getAllRegressions().values()) {
                addEvent(result, regressionResult.getEvent(), pattern, output, verbose);

            }

        } else {
            result.addAll(rateRegression.getNonRegressions());
            result.addAll(rateRegression.getAllNewEvents().values());

            for (RegressionResult regressionResult : rateRegression.getAllRegressions().values()) {
                result.add(regressionResult.getEvent());
            }
        }

        return result;
    }

    private static ReportVolume getReportVolume(ApiClient apiClient,
                                                RegressionInput input, RateRegression rateRegression,
                                                int limit, String regexFilter, PrintStream output, boolean verbose) {

        ReportVolume result = new ReportVolume();

        Pattern pattern;

        if ((regexFilter != null) && (regexFilter.length() > 0)) {
            pattern = Pattern.compile(regexFilter);
        } else {
            pattern = null;
        }

        Collection<EventResult> eventsSet = filterEvents(rateRegression, pattern, output, verbose);
        List<EventResult> events = getSortedEventsByVolume(eventsSet);

        if (pattern != null) {
            result.filter = eventsSet;
        }

        result.topEvents = new ArrayList<>();

        for (EventResult event : events) {
            if (event.stats != null) {
                if (result.topEvents.size() < limit) {
                    String arcLink = ProcessQualityGates.getArcLink(apiClient, event.id, input, rateRegression.getActiveWndowStart());
                    result.topEvents.add(new OOReportEvent(event, arcLink));
                }
            }
        }

        return result;
    }


    public QualityReport build() {
        boolean checkMaxEventGate = config.getMaxErrorVolume() != 0;
        boolean checkUniqueEventGate = config.getMaxUniqueErrors() != 0;


        QualityGateReport qualityGateReport = new QualityGateReport();
        if (config.isSomeGateBesideRegressionToProcess()) {
            //TODO fix if call fails case
            qualityGateReport = ProcessQualityGates.processCICDInputs(apiClient, input, config.isNewEvents(),
                    config.isResurfacedErrors(), config.getRegexFilter(), config.getPrintTopIssues(),
                    config.isCountGatePresent(), output, config.isDebug());
        }

        //run the regression gate
        ReportVolume reportVolume;
        RateRegression rateRegression = null;
        List<OOReportRegressedEvent> regressions = null;
        boolean hasRegressions = false;
        if (config.isRegressionPresent()) {
            rateRegression = RegressionUtil.calculateRateRegressions(apiClient, input, output, config.isDebug());

            reportVolume = getReportVolume(apiClient, input,
                    rateRegression, config.getPrintTopIssues(), config.getRegexFilter(), output, config.isDebug());

            regressions = getAllRegressions(apiClient, input, rateRegression, reportVolume.filter);
            if (regressions != null && regressions.size() > 0) {
                hasRegressions = true;
                replaceSourceId(regressions);
            }
        }

        //max total error gate
        boolean maxVolumeExceeded = (config.getMaxErrorVolume() != 0) && (qualityGateReport.getTotalErrorCount() > config.getMaxErrorVolume());

        //max unique error gate
        long uniqueEventCount;
        boolean maxUniqueErrorsExceeded;
        if (config.getMaxUniqueErrors() != 0) {
            uniqueEventCount = qualityGateReport.getUniqueErrorCount();
            maxUniqueErrorsExceeded = uniqueEventCount > config.getMaxUniqueErrors();
        } else {
            uniqueEventCount = 0;
            maxUniqueErrorsExceeded = false;
        }

        QualityGatesBesideRegressionProcess qualityGatesBesideRegressionProcess = new QualityGatesBesideRegressionProcess(qualityGateReport).invoke();

        boolean checkCritical = false;
        if (input.criticalExceptionTypes != null && input.criticalExceptionTypes.size() > 0) {
            checkCritical = true;
        }

        boolean unstable = (hasRegressions)
                || (maxVolumeExceeded)
                || (maxUniqueErrorsExceeded)
                || qualityGatesBesideRegressionProcess.isErrorsDetected();

        return new QualityReport(input, rateRegression, regressions,
                qualityGateReport.getCriticalErrors(), qualityGateReport.getTopErrors(), qualityGateReport.getNewErrors(),
                qualityGateReport.getResurfacedErrors(), qualityGateReport.getTotalErrorCount(),
                qualityGateReport.getUniqueErrorCount(), unstable, config.isNewEvents(), config.isResurfacedErrors(), checkCritical, checkMaxEventGate,
                checkUniqueEventGate, config.isRegressionPresent(), config.getMaxErrorVolume(), config.getMaxUniqueErrors(), config.isMarkUnstable());
    }

    // for each event, replace the source ID in the ARC link with 58 (which means TeamCity)
    // see: https://overopshq.atlassian.net/wiki/spaces/PP/pages/1529872385/Hit+Sources
    private static void replaceSourceId(List<? extends OOReportEvent> events) {
        String match = "&source=(\\d)+";  // matches at least one digit
        String replace = "&source=58";    // replace with 58

        for (OOReportEvent event : events) {
            String arcLink = event.getARCLink().replaceAll(match, replace);
            event.setArcLink(arcLink);
        }
    }

    private static List<OOReportRegressedEvent> getAllRegressions(ApiClient apiClient,
                                                                  RegressionInput input, RateRegression rateRegression, Collection<EventResult> filter) {

        List<OOReportRegressedEvent> result = new ArrayList<OOReportRegressedEvent>();

        for (RegressionResult regressionResult : rateRegression.getCriticalRegressions().values()) {

            if ((filter != null) && (!filter.contains(regressionResult.getEvent()))) {
                continue;
            }

            String arcLink = ProcessQualityGates.getArcLink(apiClient, regressionResult.getEvent().id, input, rateRegression.getActiveWndowStart());

            OOReportRegressedEvent regressedEvent = new OOReportRegressedEvent(regressionResult.getEvent(),
                    regressionResult.getBaselineHits(), regressionResult.getBaselineInvocations(), RegressionStringUtil.SEVERE_REGRESSION, arcLink);

            result.add(regressedEvent);
        }

        for (RegressionResult regressionResult : rateRegression.getAllRegressions().values()) {

            if (rateRegression.getCriticalRegressions().containsKey(regressionResult.getEvent().id)) {
                continue;
            }

            String arcLink = ProcessQualityGates.getArcLink(apiClient, regressionResult.getEvent().id, input, rateRegression.getActiveWndowStart());

            OOReportRegressedEvent regressedEvent = new OOReportRegressedEvent(regressionResult.getEvent(),
                    regressionResult.getBaselineHits(), regressionResult.getBaselineInvocations(), RegressionStringUtil.REGRESSION, arcLink);

            result.add(regressedEvent);
        }

        return result;
    }

    private static class ReportVolume {
        protected List<OOReportEvent> topEvents;
        protected Collection<EventResult> filter;
    }

    private class QualityGatesBesideRegressionProcess {
        private QualityGateReport qualityGateReport;
        private boolean newErrors;
        private boolean resurfaced;
        private boolean critical;

        public QualityGatesBesideRegressionProcess(QualityGateReport qualityGateReport) {
            this.qualityGateReport = qualityGateReport;
        }

        public boolean isErrorsDetected() {
            return  newErrors || resurfaced || critical;
        }

        public QualityGatesBesideRegressionProcess invoke() {
            newErrors = processQualityGateErrors(qualityGateReport.getNewErrors());
            resurfaced = processQualityGateErrors(qualityGateReport.getResurfacedErrors());
            critical = processQualityGateErrors(qualityGateReport.getCriticalErrors());
            processQualityGateErrors(qualityGateReport.getTopErrors());
            return this;
        }

        private boolean processQualityGateErrors(List<OOReportEvent> events) {
            boolean errorsExist = false;
            if (events != null && events.size() > 0) {
                errorsExist = true;
                replaceSourceId(events);
            }
            return errorsExist;
        }
    }
}
