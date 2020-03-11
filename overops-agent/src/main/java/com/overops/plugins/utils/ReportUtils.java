package com.overops.plugins.utils;

import com.overops.plugins.model.OverOpsReportModel;
import com.overops.plugins.model.ReportEventModel;
import com.overops.plugins.service.impl.ReportBuilder;
import com.takipi.api.client.util.cicd.OOReportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportUtils {

    private final static String STRING_FORMAT = "%,d";

    public static OverOpsReportModel copyResult(ReportBuilder.QualityReport report) {
        OverOpsReportModel result = new OverOpsReportModel();
        result.setMarkedUnstable(report.isMarkedUnstable());
        result.setUnstable(report.getUnstable());
        result.setSummary(getSummary(report));
        result.setPassedNewErrorGate(getPassedNewErrorGate(report));
        result.setCheckNewEvents(getCheckNewEvents(report));
        result.setNewErrorSummary(getNewErrorSummary(report));
        result.setNewEvents(getNewEvents(report).stream().map(e -> new ReportEventModel(e.getARCLink(), e.getType(), e.getIntroducedBy(), e.getEventSummary(), e.getEventRate(), e.getHits(), e.getCalls(), e.getApplications())).collect(Collectors.toList()));
        result.setPassedResurfacedErrorGate(getPassedResurfacedErrorGate(report));
        result.setCheckResurfacedEvents(getCheckResurfacedEvents(report));
        result.setResurfacedErrorSummary(getResurfacedErrorSummary(report));
        result.setResurfacedEvents(getResurfacedEvents(report).stream().map(e -> new ReportEventModel(e.getARCLink(), e.getType(), e.getIntroducedBy(), e.getEventSummary(), e.getEventRate(), e.getHits(), e.getCalls(), e.getApplications())).collect(Collectors.toList()));
        result.setCheckCriticalErrors(getCheckCriticalErrors(report));
        result.setPassedCriticalErrorGate(getPassedCriticalErrorGate(report));
        result.setCriticalErrorSummary(getCriticalErrorSummary(report));
        result.setCriticalEvents(getCriticalEvents(report).stream().map(e -> new ReportEventModel(e.getARCLink(), e.getType(), e.getIntroducedBy(), e.getEventSummary(), e.getEventRate(), e.getHits(), e.getCalls(), e.getApplications())).collect(Collectors.toList()));
        result.setCheckTotalErrors(getCheckTotalErrors(report));
        result.setPassedTotalErrorGate(getPassedTotalErrorGate(report));
        result.setTotalErrorSummary(getTotalErrorSummary(report));
        result.setCheckUniqueErrors(getCheckUniqueErrors(report));
        result.setHasTopErrors(getHasTopErrors(report));
        result.setPassedUniqueErrorGate(getPassedUniqueErrorGate(report));
        result.setUniqueErrorSummary(getUniqueErrorSummary(report));
        result.setTopEvents(getTopEvents(report).stream().map(e -> new ReportEventModel(e.getARCLink(), e.getType(), e.getIntroducedBy(), e.getEventSummary(), e.getEventRate(), e.getHits(), e.getCalls(), e.getApplications())).collect(Collectors.toList()));
        result.setRegressionSumarry(getRegressionSumarry(report));
        result.setCheckRegressedErrors(getCheckRegressedErrors(report));
        result.setPassedRegressedEvents(getPassedRegressedEvents(report));
        result.setRegressedEvents(getRegressedEvents(report).stream().map(e -> new ReportEventModel(e.getARCLink(), e.getType(), e.getIntroducedBy(), e.getEventSummary(), e.getEventRate(), e.getHits(), e.getCalls(), e.getApplications())).collect(Collectors.toList()));

        result.setNewGateTotal(String.format(STRING_FORMAT, report.getNewIssues() != null ? report.getNewIssues().size() : 0));
        result.setResurfacedGateTotal(String.format(STRING_FORMAT, report.getResurfacedErrors() != null ? report.getResurfacedErrors().size() : 0));
        result.setCriticalGateTotal(String.format(STRING_FORMAT, report.getCriticalErrors() != null ? report.getCriticalErrors().size() : 0));
        result.setTotalGateTotal(String.format(STRING_FORMAT, report.getEventVolume()));
        result.setUniqueGateTotal(String.format(STRING_FORMAT, report.getUniqueEventsCount()));
        result.setRegressionGateTotal(String.format(STRING_FORMAT, report.getRegressions() != null ? report.getRegressions().size() : 0));

        // hide "total" column in summary table on old reports which don't have this data saved
        result.setHasTotal(true);

        return result;
    }

    public static OverOpsReportModel exceptionResult(Exception exception) {
        OverOpsReportModel result = new OverOpsReportModel();
        result.setException(exception);
        return result;
    }

    private static String getSummary(ReportBuilder.QualityReport report) {
        if (report.getUnstable() && report.isMarkedUnstable()) {
            // the build is unstable when marking the build as unstable
            // teamcity has no "unstable" status like Jenkins, so we're using "failure"
            return "OverOps has marked build "+ report.getDeploymentName() + " as \"failure\".";
        } else if (report.getUnstable() && !report.isMarkedUnstable()) {
            //unstable build stable when NOT marking the build as unstable
            return "OverOps has detected issues with build "+ report.getDeploymentName() + " but did not mark the build as \"failure\".";
        } else {
            //stable build when marking the build as unstable
            return "Congratulations, build " + report.getDeploymentName() + " has passed all quality gates!";
        }
    }

    private static boolean getPassedNewErrorGate(ReportBuilder.QualityReport report) {
        return getCheckNewEvents(report) && !getNewErrorsExist(report);
    }

    private static boolean getCheckNewEvents(ReportBuilder.QualityReport report) {
        return report.isCheckNewGate();
    }

    private static String getNewErrorSummary(ReportBuilder.QualityReport report) {
        if (getNewEvents(report).size() > 0) {
            int count = report.getNewIssues().size();
            StringBuilder sb = new StringBuilder("New Error Gate: Failed, OverOps detected ");
            sb.append(count);
            sb.append(" new error");
            if (count != 1) {
              sb.append("s");
            }
            sb.append(" in your build.");
            return sb.toString();
        } else if (report.isCheckNewGate()) {
            return "New Error Gate: Passed, OverOps did not detect any new errors in your build.";
        }

        return null;
    }

    private static boolean getNewErrorsExist(ReportBuilder.QualityReport report) {
        return getNewEvents(report).size() > 0;
    }

    private static List<OOReportEvent> getNewEvents(ReportBuilder.QualityReport report) {
        return Optional.ofNullable(report.getNewIssues()).orElse(new ArrayList<>());
    }

    private static boolean getPassedResurfacedErrorGate(ReportBuilder.QualityReport report) {
        return getCheckResurfacedEvents(report) && !getResurfacedErrorsExist(report);
    }

    private static boolean getResurfacedErrorsExist(ReportBuilder.QualityReport report) {
        return getResurfacedEvents(report).size() > 0;
    }

    private static boolean getCheckResurfacedEvents(ReportBuilder.QualityReport report) {
        return report.isCheckResurfacedGate();
    }

    private static String getResurfacedErrorSummary(ReportBuilder.QualityReport report) {
        if (getResurfacedEvents(report).size() > 0) {
            return "Resurfaced Error Gate: Failed, OverOps detected " + report.getResurfacedErrors().size() + " resurfaced errors in your build.";
        } else if (report.isCheckResurfacedGate()) {
            return "Resurfaced Error Gate: Passed, OverOps did not detect any resurfaced errors in your build.";
        }

        return null;
    }

    private static List<OOReportEvent> getResurfacedEvents(ReportBuilder.QualityReport report) {
        return Optional.ofNullable(report.getResurfacedErrors()).orElse(new ArrayList<>());
    }

    private static boolean getCheckCriticalErrors(ReportBuilder.QualityReport report) {
        return report.isCheckCriticalGate();
    }

    private static boolean getPassedCriticalErrorGate(ReportBuilder.QualityReport report) {
        return getCheckCriticalErrors(report) && !getCriticalErrorsExist(report);
    }

    private static boolean getCriticalErrorsExist(ReportBuilder.QualityReport report) {
        return getCriticalEvents(report).size() > 0;
    }

    private static String getCriticalErrorSummary(ReportBuilder.QualityReport report) {
        if (getCriticalEvents(report).size() > 0) {
            return "Critical Error Gate: Failed, OverOps detected " + report.getCriticalErrors().size() + " critical errors in your build.";
        } else if (report.isCheckCriticalGate()) {
            return "Critical Error Gate: Passed, OverOps did not detect any critical errors in your build.";
        }

        return null;
    }

    private static List<OOReportEvent> getCriticalEvents(ReportBuilder.QualityReport report) {
        return Optional.ofNullable(report.getCriticalErrors()).orElse(new ArrayList<>());
    }

    private static boolean getCheckTotalErrors(ReportBuilder.QualityReport report) {
        return report.isCheckVolumeGate();
    }

    private static boolean getPassedTotalErrorGate(ReportBuilder.QualityReport report) {
        return getCheckTotalErrors(report) && (report.getEventVolume() > 0 && report.getEventVolume() < report.getMaxEventVolume());
    }

    private static String getTotalErrorSummary(ReportBuilder.QualityReport report) {
        if (report.getEventVolume() > 0 && report.getEventVolume() >= report.getMaxEventVolume()) {
            return "Total Error Volume Gate: Failed, OverOps detected " + report.getEventVolume() + " total errors which is >= the max allowable of " + report.getMaxEventVolume();
        } else if (report.getEventVolume() > 0 && report.getEventVolume() < report.getMaxEventVolume()) {
            return "Total Error Volume Gate: Passed, OverOps detected " + report.getEventVolume() + " total errors which is < than max allowable of " + report.getMaxEventVolume();
        }

        return null;
    }

    private static boolean getCheckUniqueErrors(ReportBuilder.QualityReport report) {
        return report.isCheckUniqueGate();
    }

    private static boolean getHasTopErrors(ReportBuilder.QualityReport report) {
        return !getPassedTotalErrorGate(report) || !getPassedUniqueErrorGate(report);
    }

    private static boolean getPassedUniqueErrorGate(ReportBuilder.QualityReport report) {
        return getCheckUniqueErrors(report) && (report.getUniqueEventsCount() > 0 && report.getUniqueEventsCount() < report.getMaxUniqueVolume());
    }

    private static String getUniqueErrorSummary(ReportBuilder.QualityReport report) {
        if (report.getUniqueEventsCount() > 0 && report.getUniqueEventsCount() >= report.getMaxUniqueVolume()) {
            return "Unique Error Volume Gate: Failed, OverOps detected " + report.getUniqueEventsCount() + " unique errors which is >= the max allowable of " + report.getMaxUniqueVolume();
        } else if (report.getUniqueEventsCount() > 0 && report.getUniqueEventsCount() < report.getMaxUniqueVolume()) {
            return "Unique Error Volume Gate: Passed, OverOps detected " + report.getUniqueEventsCount() + " unique errors which is < than max allowable of " + report.getMaxUniqueVolume();
        }

        return null;
    }

    private static List<OOReportEvent> getTopEvents(ReportBuilder.QualityReport report) {
        return Optional.ofNullable(report.getTopErrors()).orElse(new ArrayList<>());
    }

    private static String getRegressionSumarry(ReportBuilder.QualityReport report) {
        String baselineTime = Objects.nonNull(report.getInput()) ? report.getInput().baselineTime : "";
        if (!getPassedRegressedEvents(report)) {
            return "Increasing Quality Gate: Failed, OverOps detected increasing errors in the current build against the baseline of " + baselineTime;
        } else if (getPassedRegressedEvents(report)) {
            return "Increasing Quality Gate: Passed, OverOps did not detect any increasing errors in the current build against the baseline of " + baselineTime;
        }

        return null;
    }

    private static boolean getCheckRegressedErrors(ReportBuilder.QualityReport report) {
        return report.isCheckRegressionGate();
    }

    private static boolean getPassedRegressedEvents(ReportBuilder.QualityReport report) {
        return !(getCheckRegressedErrors(report) && report.getRegressions() != null && report.getRegressions().size() > 0);
    }

    private static List<OOReportEvent> getRegressedEvents(ReportBuilder.QualityReport report) {
        return Optional.ofNullable(report.getAllIssues()).orElse(new ArrayList<>());
    }

}
