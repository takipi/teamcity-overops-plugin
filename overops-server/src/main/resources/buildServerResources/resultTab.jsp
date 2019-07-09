<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout"%>
<%--@elvariable id="result" type="com.overops.plugins.model.OverOpsReportModel"--%>
<style>
    .overops-report table, .overops-report th, .overops-report td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    .overops-report th, .overops-report td {
        padding: 5px;
        text-align: left;
    }
    .failed {
        color: #ff0000;
    }
    .passed {
        color: #008000;
    }
    .orange {
        color: #FFA500;
    }
</style>
<div class="overops-report">
    <h1><img src ="${teamcityPluginResourcesPath}img/logo_regular.svg" height="32" style="margin-bottom: -8px"/> Quality Report</h1>
    <c:if test="${result.markedUnstable}">
        <c:if test="${result.unstable}">
            <div >
                <h2 class="failed">${result.summary}</h2>
            </div>
        </c:if>
        <c:if test="${!result.unstable}">
            <div >
                <h2 class="passed">${result.summary}</h2>
            </div>
        </c:if>
    </c:if>
    <c:if test="${!result.markedUnstable}">
        <c:if test="${result.unstable}">
            <div >
                <h2 class="orange">${result.summary}</h2>
            </div>
        </c:if>
        <c:if test="${!result.unstable}">
            <div >
                <h2 class="passed">${result.summary}</h2>
            </div>
        </c:if>
    </c:if>
    <c:if test="${result.checkNewEvents}">
        <table style="width:100%">
            <c:if test="${result.passedNewErrorGate}">
                <tr style="font-weight:bold"><td style="color:#008000">${result.newErrorSummary}</td></tr>
                <table style="width:100%">
                    <tr><td>Nothing to report</td></tr>
                </table>
            </c:if>
            <c:if test="${!result.passedNewErrorGate}">
                <tr style="font-weight:bold"><td style="color:#ff0000">${result.newErrorSummary}</td></tr>
                <table style="width:100%">
                    <tr style="font-weight:bold"><td>Event</td><td>Application(s)</td><td>Introduced by</td><td>Volume</td></tr>
                    <c:forEach items="${result.newEvents}" var="k">
                        <tr><td><a href="${k.arcLink}" target="_blank"> ${k.eventSummary}</a></td><td>${k.applications}</td><td>${k.introducedBy}</td><td>${k.hits}</td></tr>
                    </c:forEach>
                </table>
            </c:if>
        </table>
        <h2> </h2>
    </c:if>
    <c:if test="${result.checkResurfacedEvents}">
        <table style="width:100%">
            <c:if test="${result.passedResurfacedErrorGate}">
                <tr style="font-weight:bold"><td style="color:#008000">${result.resurfacedErrorSummary}</td></tr>
                <table style="width:100%">
                    <tr><td>Nothing to report</td></tr>
                </table>
            </c:if>
            <c:if test="${!result.passedResurfacedErrorGate}">
                <tr style="font-weight:bold"><td style="color:#ff0000">${result.resurfacedErrorSummary}</td></tr>
                <table style="width:100%">
                    <tr style="font-weight:bold"><td>Event</td><td>Application(s)</td><td>Introduced by</td><td>Volume</td></tr>
                    <c:forEach items="${result.resurfacedEvents}" var="k">
                        <tr><td><a href="${k.arcLink}" target="_blank"> ${k.eventSummary}</a></td><td>${k.applications}</td><td>${k.introducedBy}</td><td>${k.hits}</td></tr>
                    </c:forEach>
                </table>
            </c:if>
        </table>
        <h2> </h2>
    </c:if>
    <c:if test="${result.checkTotalErrors || result.checkUniqueErrors}">
        <table style="width:100%">
            <c:if test="${result.checkTotalErrors}">
                <c:if test="${result.passedTotalErrorGate}">
                    <tr style="font-weight:bold"><td style="color:#008000">${result.totalErrorSummary}</td></tr>
                </c:if>
                <c:if test="${!result.passedTotalErrorGate}">
                    <tr style="font-weight:bold"><td style="color:#ff0000">${result.totalErrorSummary}</td></tr>
                </c:if>
            </c:if>
            <c:if test="${result.checkUniqueErrors}">
                <c:if test="${result.passedUniqueErrorGate}">
                    <tr style="font-weight:bold"><td style="color:#008000">${result.uniqueErrorSummary}</td></tr>
                </c:if>
                <c:if test="${!result.passedUniqueErrorGate}">
                    <tr style="font-weight:bold"><td style="color:#ff0000">${result.uniqueErrorSummary}</td></tr>
                </c:if>
            </c:if>
            <c:if test="${result.hasTopErrors}">
                <table style="width:100%">
                    <tr style="font-weight:bold"><td>Top Events Affecting Unique/Total Error Gates</td><td>Application(s)</td><td>Introduced by</td><td>Volume</td></tr>
                    <c:forEach items="${result.topEvents}" var="k">
                        <tr><td><a href="${k.arcLink}" target="_blank"> ${k.eventSummary}</a></td><td>${k.applications}</td><td>${k.introducedBy}</td><td>${k.hits}</td></tr>
                    </c:forEach>
                </table>
            </c:if>
            <c:if test="${!result.hasTopErrors}">
                <table style="width:100%">
                    <tr><td>Nothing to report</td></tr>
                </table>
            </c:if>
        </table>
    </c:if>
    <c:if test="${result.checkCriticalErrors}">
        <table style="width:100%">
            <c:if test="${result.passedCriticalErrorGate}">
                <tr style="font-weight:bold"><td style="color:#008000">${result.criticalErrorSummary}</td></tr>
                <table style="width:100%">
                    <tr><td>Nothing to report</td></tr>
                </table>
            </c:if>
            <c:if test="${!result.passedCriticalErrorGate}">
                <tr style="font-weight:bold"><td style="color:#ff0000">${result.criticalErrorSummary}</td></tr>
                <table style="width:100%">
                    <tr style="font-weight:bold"><td>Event</td><td>Application(s)</td><td>Introduced by</td><td>Volume</td></tr>
                    <c:forEach items="${result.criticalEvents}" var="k">
                        <tr><td><a href="${k.arcLink}" target="_blank"> ${k.eventSummary}</a></td><td>${k.applications}</td><td>${k.introducedBy}</td><td>${k.hits}</td></tr>
                    </c:forEach>
                </table>
            </c:if>
        </table>
    </c:if>
    <c:if test="${result.checkRegressedErrors}">
        <table style="width:100%">
            <c:if test="${result.passedRegressedEvents}">
                <tr style="font-weight:bold"><td style="color:#008000">${result.regressionSumarry}</td></tr>
                <table style="width:100%">
                    <tr><td>Nothing to report</td></tr>
                </table>
            </c:if>
            <c:if test="${!result.passedRegressedEvents}">
                <tr style="font-weight:bold"><td style="color:#ff0000">${result.regressionSumarry}</td></tr>
                <table style="width:100%">
                    <tr style="font-weight:bold"><td>Event</td><td>Application(s)</td><td>Introduced by</td><td>Volume / Rate</td><td>Type</td> </tr>
                    <c:forEach items="${result.regressedEvents}" var="i">
                        <tr><td><a href="${i.arcLink}" target="_blank"> ${i.eventSummary}</a></td><td>${k.applications}</td><td>${i.introducedBy}</td><td>${i.eventRate}</td><td>${i.type}</td></tr>
                    </c:forEach>
                </table>
            </c:if>
        </table>
    </c:if>
</div>
