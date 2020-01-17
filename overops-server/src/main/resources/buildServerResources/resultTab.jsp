<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%--@elvariable id="result" type="com.overops.plugins.model.OverOpsReportModel"--%>
<style>
    .overops-quality-report a {
        color: #333;
        text-decoration: none;
    }

    .overops-quality-report a:hover {
        text-decoration: underline;
    }

    .overops-quality-report h1 {
        margin: 0;
        font-size: 22px;
    }

    .overops-quality-report h4 {
        margin: 0;
        font-size: 14px;
    }

    .overops-quality-report p {
        line-height: 1;
        margin: 4px 0 16px 0;
    }

    .overops-quality-report .d-flex {
        display: flex;
    }

    .overops-quality-report .align-center {
        align-items: center;
    }

    .overops-quality-report .mr-1 {
        margin-right: 8px;
    }

    .overops-quality-report .mr-2 {
        margin-right: 16px;
    }

    .overops-quality-report .mx-1 {
        margin-right: 8px;
        margin-left: 8px;
    }

    .overops-quality-report .ml-2 {
        margin-left: 32px;
    }

    .overops-quality-report .mb-2 {
        margin-bottom: 16px;
    }

    .overops-quality-report .mt-3 {
        margin-top: 32px;
    }

    .overops-quality-report .mt-4 {
        margin-top: 40px;
    }

    .overops-quality-report .alert {
        margin: 32px 0;
        padding: 8px 0;
        border-color: transparent;
        border-width: 2px;
        border-radius: 3px;
        border-style: solid;
        font-size: 14px;
        color: #333;
    }

    .overops-quality-report .alert-success {
        background-color: hsl(78, 100%, 97%);
        border-color: hsl(81, 51%, 76%);
    }

    .overops-quality-report .alert-warning {
        background-color: hsl(55, 75.0%, 96.9%);
        border-color: hsl(55, 51%, 76%);
    }

    .overops-quality-report .alert-danger {
        background-color: hsl(10, 75.0%, 96.9%);
        border-color: hsl(7, 65.9%, 82.7%);
    }

    .overops-quality-report [class^=icon-] {
        display: inline-block;
        height: 24px;
        width: 24px;
        background-repeat: no-repeat;
        background-size: 24px 24px;
    }

    .overops-quality-report .icon-lg {
        height: 48px;
        width: 48px;
        background-size: 48px 48px;
    }

    .overops-quality-report .icon-success {
        background-image: url("${teamcityPluginResourcesPath}img/icon-success.svg");
    }

    .overops-quality-report .icon-warning {
        background-image: url("${teamcityPluginResourcesPath}img/icon-warning.svg");
    }

    .overops-quality-report .icon-danger {
        background-image: url("${teamcityPluginResourcesPath}img/icon-danger.svg");
    }

    .overops-quality-report .table {
        border-collapse: collapse;
        width: 100%;
    }

    .overops-quality-report .table thead th {
        vertical-align: bottom;
        text-align: left;
        white-space: nowrap;
        background-color: hsl(0, 0%, 81%);
        font-size: 14px;
    }

    .overops-quality-report .table td,
    .overops-quality-report .table th {
        line-height: 1;
        padding: .75rem;
        vertical-align: top;
        white-space: nowrap;
        max-width: calc(50vw - 360px); /* jenkins sidebar is 360px wide */
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .overops-quality-report .table-striped tbody tr:nth-of-type(even) {
        background-color: hsl(216, 100%, 98%);
    }

    .overops-quality-report .muted {
        color: hsl(0, 0%, 60%);
    }

    .table-striped {
        border-color: #acacac;
        border-width: 1px;
    }

    .table-summary {
        border-collapse: collapse;
        width: 278px;
    }

    .red-color {
        color: #ee6557;
    }

    .New-section {
        border-color: #acacac;
    }

    .Resurfaced-section {
        border-color: #acacac;
    }

    .Critical-section {
        border-color: #acacac;
    }

    .Increasing-section {
        border-color: #acacac;
    }
</style>
<script type="text/javascript">
    BS.OOResultTab = {
        scrollTo: function (classPart) {
            var element = null;
            var array = document.querySelectorAll("[class*='" + classPart + "'");
            if (array != null && array.length > 0) {
                element = array[0];
            }
            this.scrollToResolver(element);
        },
        scrollToResolver: function (element) {
            if (element == null) {
                return;
            }

            var maxCurrent = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
            var distanceToCover = element.getBoundingClientRect().top - maxCurrent;
            var i;
            var bodyScrollTopBeginning = document.body.scrollTop;
            var documentElementScrollTopBeginning = document.documentElement.scrollTop;
            var pi2 = Math.PI / 2;
            for (i = 20; i <= 100; i += 10) {
                var addition = Math.sin(pi2 * (i / 100.0)) * distanceToCover;
                document.body.scrollTop += bodyScrollTopBeginning + addition;
                document.documentElement.scrollTop += documentElementScrollTopBeginning + addition;
                setTimeout(function () {
                }, 55);
            }
        }
    }
</script>
<section class="overops-quality-report">

    <div class="d-flex mt-3">
        <img src="${teamcityPluginResourcesPath}img/overops-logo.svg" alt="OverOps" class="mr-2"/>
        <h1>Quality Report</h1>
    </div>

    <c:choose>
        <c:when test="${result.markedUnstable}">
            <c:choose>
                <c:when test="${result.unstable}">
                    <div class="alert alert-danger d-flex align-center">
                        <i class="icon-danger icon-lg mx-1"></i>
                            ${result.summary}
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-success d-flex align-center">
                        <i class="icon-success icon-lg mx-1"></i>
                            ${result.summary}
                    </div>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${result.unstable}">
                    <div class="alert alert-warning d-flex align-center">
                        <i class="icon-warning icon-lg mx-1"></i>
                            ${result.summary}
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-success d-flex align-center">
                        <i class="icon-success icon-lg mx-1"></i>
                            ${result.summary}
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

    <div class="d-flex mt-3">
        <h2>Report Summary</h2>
    </div>

    <div class="table-summary">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Gate</th>
                <th>Status</th>
                <th>Total</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${result.summaryTable}" var="sr">
                <tr>
                    <td>
                        <a onclick="BS.OOResultTab.scrollTo('${sr.gateName}-section')">${sr.gateName}</a>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${sr.passed}">
                                <h4>${sr.gateStatus}</h4>
                            </c:when>
                            <c:otherwise>
                                <h4 class="red-color">${sr.gateStatus}</h4>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${sr.total}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>


    <c:if test="${result.checkNewEvents}">
        <c:choose>
            <c:when test="${result.passedNewErrorGate}">
                <div class="d-flex align-center mt-4 New-section">
                    <i class="icon-success mr-1"></i>
                    <h4>${result.newErrorSummary}</h4>
                </div>
                <p class="ml-2 muted">Nothing to report</p>
            </c:when>
            <c:otherwise>
                <div class="d-flex align-center mb-2 mt-4 New-section">
                    <i class="icon-danger mr-1"></i>
                    <h4>${result.newErrorSummary}</h4>
                </div>
                <table class="table table-striped>
                <thead>
                <tr>
                <th>Event</th>
                <th>Application(s)</th>
                <th>Introduced by</th>
                <th>Volume</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${result.newEvents}" var="k">
                    <tr>
                    <td>
                    <a href="${k.arcLink}" target="_blank">${k.eventSummary}</a>
                    </td>
                    <td>${k.applications}</td>
                    <td>${k.introducedBy}</td>
                    <td>${k.hits}</td>
                    </tr>
                </c:forEach>
                </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${result.checkResurfacedEvents}">
        <c:choose>
            <c:when test="${result.passedResurfacedErrorGate}">
                <div class="d-flex align-center mt-4 Resurfaced-section">
                    <i class="icon-success mr-1"></i>
                    <h4>${result.resurfacedErrorSummary}</h4>
                </div>
                <p class="ml-2 muted">Nothing to report</p>
            </c:when>
            <c:otherwise>
                <div class="d-flex align-center mb-2 mt-4 Resurfaced-section">
                    <i class="icon-danger mr-1"></i>
                    <h4>${result.resurfacedErrorSummary}</h4>
                </div>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Event</th>
                        <th>Application(s)</th>
                        <th>Introduced by</th>
                        <th>Volume</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result.resurfacedEvents}" var="k">
                        <tr>
                            <td>
                                <a href="${k.arcLink}" target="_blank">${k.eventSummary}</a>
                            </td>
                            <td>${k.applications}</td>
                            <td>${k.introducedBy}</td>
                            <td>${k.hits}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${result.checkTotalErrors || result.checkUniqueErrors}">
        <c:if test="${result.checkTotalErrors}">
            <c:choose>
                <c:when test="${result.passedTotalErrorGate}">
                    <div class="d-flex align-center mt-4 mb-2">
                        <i class="icon-success mr-1"></i>
                        <h4>${result.totalErrorSummary}</h4>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="d-flex align-center mt-4 mb-2">
                        <i class="icon-danger mr-1"></i>
                        <h4>${result.totalErrorSummary}</h4>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <c:if test="${result.checkUniqueErrors}">
            <c:choose>
                <c:when test="${result.passedUniqueErrorGate}">
                    <div class="d-flex align-center mt-4 mb-2">
                        <i class="icon-success mr-1"></i>
                        <h4>${result.uniqueErrorSummary}</h4>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="d-flex align-center mt-4 mb-2">
                        <i class="icon-danger mr-1"></i>
                        <h4>${result.uniqueErrorSummary}</h4>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <c:choose>
            <c:when test="${result.hasTopErrors}">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Top Events Affecting Unique/Total Error Gates</th>
                        <th>Application(s)</th>
                        <th>Introduced by</th>
                        <th>Volume</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result.topEvents}" var="k">
                        <tr>
                            <td>
                                <a href="${k.arcLink}" target="_blank">${k.eventSummary}</a>
                            </td>
                            <td>${k.applications}</td>
                            <td>${k.introducedBy}</td>
                            <td>${k.hits}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="ml-2 muted">Nothing to report</p>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${result.checkCriticalErrors}">
        <c:choose>
            <c:when test="${result.passedCriticalErrorGate}">
                <div class="d-flex align-center mt-4 Critical-section">
                    <i class="icon-success mr-1"></i>
                    <h4>${result.criticalErrorSummary}</h4>
                </div>
                <p class="ml-2 muted">Nothing to report</p>
            </c:when>
            <c:otherwise>
                <div class="d-flex align-center mb-2 mt-4 Critical-section">
                    <i class="icon-danger mr-1"></i>
                    <h4>${result.criticalErrorSummary}</h4>
                </div>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Event</th>
                        <th>Application(s)</th>
                        <th>Introduced by</th>
                        <th>Volume</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result.criticalEvents}" var="k">
                        <tr>
                            <td>
                                <a href="${k.arcLink}" target="_blank">${k.eventSummary}</a>
                            </td>
                            <td>${k.applications}</td>
                            <td>${k.introducedBy}</td>
                            <td>${k.hits}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${result.checkRegressedErrors}">
        <c:choose>
            <c:when test="${result.passedRegressedEvents}">
                <div class="d-flex align-center mt-4 Increasing-section">
                    <i class="icon-success mr-1"></i>
                    <h4>${result.regressionSumarry}</h4>
                </div>
                <p class="ml-2 muted">Nothing to report</p>
            </c:when>
            <c:otherwise>
                <div class="d-flex align-center mb-2 mt-4 Increasing-section">
                    <i class="icon-danger mr-1"></i>
                    <h4>${result.regressionSumarry}</h4>
                </div>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Event</th>
                        <th>Application(s)</th>
                        <th>Introduced by</th>
                        <th>Volume / Rate</th>
                        <th>Type</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result.regressedEvents}" var="i">
                        <tr>
                            <td>
                                <a href="${i.arcLink}" target="_blank">${i.eventSummary}</a>
                            </td>
                            <td>${i.applications}</td>
                            <td>${i.introducedBy}</td>
                            <td>${i.eventRate}</td>
                            <td>${i.type}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

</section>
