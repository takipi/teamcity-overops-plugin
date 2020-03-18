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
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static com.takipi.api.client.util.regression.RegressionStringUtil.REGRESSION;
import static com.takipi.api.client.util.regression.RegressionStringUtil.SEVERE_REGRESSION;

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

    private void addIfMatchPattern(Set<EventResult> events, EventResult event, Pattern pattern) {
        if (isPatternMatched(event, pattern)) {
            events.add(event);
        } else {
            printlnForDebug(event + " did not match regexFilter and was skipped");
        }
    }

    private boolean isPatternMatched(EventResult event, Pattern pattern) {
        String json = new Gson().toJson(event);
        return  !pattern.matcher(json).find();
    }

    private void printlnForDebug(String message) {
        if (config.isDebug()) {
            output.println(message);
        }
    }

    private Collection<EventResult> filterEvents(RateRegression rateRegression, Pattern pattern) {
        Set<EventResult> result = new HashSet<>();

        if (pattern != null) {
            rateRegression.getNonRegressions().stream()
                    .forEach(eventResult -> addIfMatchPattern(result, eventResult, pattern));
            rateRegression.getAllNewEvents().values().stream()
                    .forEach(eventResult -> addIfMatchPattern(result, eventResult, pattern));
            rateRegression.getAllRegressions().values().stream()
                    .forEach(regressionResult -> addIfMatchPattern(result, regressionResult.getEvent(), pattern));
        } else {
            result.addAll(rateRegression.getNonRegressions());
            result.addAll(rateRegression.getAllNewEvents().values());
            rateRegression.getAllRegressions().values().stream()
                    .forEach(regressionResult -> result.add(regressionResult.getEvent()));
        }

        return result;
    }

    private ReportVolume getReportVolume(RateRegression rateRegression) {
        ReportVolume result = new ReportVolume();

        Pattern pattern = getPattern(config.getRegexFilter());
        Collection<EventResult> eventsSet = filterEvents(rateRegression, pattern);

        if (pattern != null) {
            result.filter = eventsSet;
        }

        eventsSet.stream()
                .filter(eventResult -> eventResult.stats != null)
                .sorted((eventResult1, eventResult2) -> (int)(eventResult2.stats.hits - eventResult1.stats.hits))
                .limit(config.getPrintTopIssues())
                .forEach(eventResult -> {
                    String arcLink = ProcessQualityGates.getArcLink(apiClient, eventResult.id, input, rateRegression.getActiveWndowStart());
                    result.topEvents.add(new OOReportEvent(eventResult, arcLink));
                });

        return result;
    }

    private Pattern getPattern(String regexFilter) {
        Pattern pattern;

        if ((regexFilter != null) && (regexFilter.length() > 0)) {
            pattern = Pattern.compile(regexFilter);
        } else {
            pattern = null;
        }
        return pattern;
    }

    public ReportCreationInfo getQualityGateReport() {
        if (config.isSomeGateBesideRegressionToProcess()) {
            try {
                QualityGateReport qualityGateReport = ProcessQualityGates.processCICDInputs(apiClient, input, config.isNewEvents(),
                        config.isResurfacedErrors(), config.getRegexFilter(), config.getPrintTopIssues(),
                        config.isCountGatePresent(), output, config.isDebug());
                return new ReportCreationInfo(qualityGateReport, false);
            } catch (Exception e) {
                printlnForDebug("Error processing CI CD inputs " + e.getMessage());
            }
        }

        return new ReportCreationInfo(new QualityGateReport(), true);
    }

    public QualityReport build() {
        ReportCreationInfo reportCreationInfo = getQualityGateReport();
        QualityGateReport qualityGateReport = reportCreationInfo.qualityGateReport;

        List<OOReportRegressedEvent> regressions = getOoReportRegressedEvents();
        boolean isRegressionsDetected = regressions != null && regressions.size() > 0;

        QualityGatesBesideRegressionProcess qualityGatesBesideRegressionProcess = new QualityGatesBesideRegressionProcess(qualityGateReport).invoke();

        boolean unstable = reportCreationInfo.isDefault
                || isRegressionsDetected
                || config.checkIfMaxVolumeExceeded(qualityGateReport.getTotalErrorCount())
                || config.checkIfMaxUniqueErrorsExceeded(qualityGateReport.getUniqueErrorCount())
                || qualityGatesBesideRegressionProcess.isErrorsDetected();

        return new QualityReport(input, regressions, qualityGateReport, unstable, config);
    }

    private List<OOReportRegressedEvent> getOoReportRegressedEvents() {
        List<OOReportRegressedEvent> regressions = null;
        if (config.isRegressionPresent()) {
            RateRegression rateRegression = RegressionUtil.calculateRateRegressions(apiClient, input, output, config.isDebug());
            ReportVolume reportVolume = getReportVolume(rateRegression);
            regressions = getAllRegressions(rateRegression, reportVolume.filter);

            if (regressions != null && regressions.size() > 0) {
                replaceSourceId(regressions);
            }
        }
        return regressions;
    }

    /**
     * for each event, replace the source ID in the ARC link with the number 4 (which means TeamCity)
     * see: https://overopshq.atlassian.net/wiki/spaces/PP/pages/1529872385/Hit+Sources
     *
     * @param events
     */
    private static void replaceSourceId(List<? extends OOReportEvent> events) {
        String match = "&source=(\\d)+";  // matches at least one digit
        String replace = "&source=58";    // replace with 58

        for (OOReportEvent event : events) {
            String arcLink = event.getARCLink().replaceAll(match, replace);
            event.setArcLink(arcLink);
        }
    }

    private List<OOReportRegressedEvent> getAllRegressions(RateRegression rateRegression, Collection<EventResult> filter) {
        List<OOReportRegressedEvent> result = new ArrayList<OOReportRegressedEvent>();

        BiConsumer<RegressionResult, String> addToResult = (regressionResult, type) -> {
            String arcLink = ProcessQualityGates.getArcLink(apiClient, regressionResult.getEvent().id, input, rateRegression.getActiveWndowStart());
            result.add(new OOReportRegressedEvent(regressionResult, type, arcLink));
        };

        if (filter != null) {
            rateRegression.getCriticalRegressions().values().stream()
                    .filter(regressionResult -> filter.contains(regressionResult.getEvent()))
                    .forEach(regressionResult -> {
                        addToResult.accept(regressionResult, SEVERE_REGRESSION);
                    });
        }

        rateRegression.getAllRegressions().values().stream()
                .filter(regressionResult -> !rateRegression.getCriticalRegressions().containsKey(regressionResult.getEvent().id))
                .forEach(regressionResult -> {
                    addToResult.accept(regressionResult, REGRESSION);
                });

        return result;
    }

    private static class ReportCreationInfo {
        public final boolean isDefault;
        public final QualityGateReport qualityGateReport;

        public ReportCreationInfo(QualityGateReport qualityGateReport, boolean isDefault) {
            this.qualityGateReport = qualityGateReport;
            this.isDefault = isDefault;
        }
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
