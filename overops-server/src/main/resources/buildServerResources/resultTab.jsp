<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout"%>
<%--@elvariable id="result" type="com.overops.plugins.model.OverOpsReportModel"--%>
<style>
  .overops-quality-report a {
    text-decoration: none;
  }
  .overops-quality-report a:hover {
    text-decoration: underline;
  }
  .overops-quality-report h1 {
    margin: 0;
    font-size: 22px;
  }
  .overops-quality-report h3 {
    margin: 0 0 0.5em;
    font-size: 16px;
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
  .overops-quality-report .mb-5 {
    margin-bottom: 64px;
  }
  .overops-quality-report .w-25 {
    width: 25%!important;
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
  .overops-quality-report .alert-muted {
    background-color: #f5f5f5;
    border-color: #ccc;
  }
  .overops-quality-report [class^=icn-] {
    display: inline-block;
    height: 24px;
    width: 24px;
    background-repeat: no-repeat;
    background-size: 24px 24px;
  }
  .overops-quality-report .icn-lg {
    height: 48px;
    width: 48px;
    background-size: 48px 48px;
  }
  .overops-quality-report .icn-td {
    height: 20px;
    width: 20px;
    background-size: 20px 20px;
    margin: -5px 5px -5px -10px;
  }
  .overops-quality-report .icn-success {
    background-image: url("${teamcityPluginResourcesPath}img/icn-success.svg");
  }
  .overops-quality-report .icn-warning {
    background-image: url("${teamcityPluginResourcesPath}img/icn-warning.svg");
  }
  .overops-quality-report .icn-danger {
    background-image: url("${teamcityPluginResourcesPath}img/icn-danger.svg");
  }
  .overops-quality-report .icn-times {
    background-image: url("${teamcityPluginResourcesPath}img/icn-times.svg");
  }
  .overops-quality-report .icn-question {
    background-image: url("${teamcityPluginResourcesPath}img/icn-question.svg");
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
  .overops-quality-report hr {
    margin: 16px 0;
  }
  .overops-quality-report code {
    font-weight: 700;
    display: inline-block;
  }
  .overops-quality-report ul.unstyled {
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .overops-quality-report ul.unstyled > li {
    margin: .5em 0;
  }
</style>
<section class="overops-quality-report">

  <div class="d-flex align-center mt-3">
    <img src ="${teamcityPluginResourcesPath}img/overops-logo.svg" alt="OverOps" class="mr-2" />
    <h1>Quality Report</h1>
  </div>

  <c:choose>
    <c:when test="${result.hasException}">
      <div class="alert alert-muted d-flex align-center">
        <i class="icn-question icn-lg mx-1"></i>
        OverOps was unable to generate a Quality Report.
      </div>
      <div class="d-flex align-center mb-2 mt-4">
        <i class="icn-question mr-1"></i>
        <h4>An error occurred while generating a Quality Report.</h4>
      </div>
      <ul class="unstyled">
        <li>
          <a href="https://support.overops.com/hc/en-us/articles/360041054474-Best-Practice-Short-lived-application-considerations" target="_blank" rel="nofollow">
            Best practices for short lived applications &rarr;
          </a>
        </li>
        <li>
          <a href="https://support.overops.com/" target="_blank" rel="nofollow">
            OverOps Support Portal &rarr;
          </a>
        </li>
        <li>
          <a href="mailto:support@overops.com?${result.emailMessage}" target="_blank" rel="nofollow">
            Contact OverOps Support &rarr;
          </a>
        </li>
      </ul>
      <hr/>
      <code class="mb-2">${result.exceptionMessage}</code>
      <pre class="muted mb-2">${result.stackTrace}</pre>
    </c:when>
    <c:otherwise>
      <c:choose>
        <c:when test="${result.markedUnstable}">
          <c:choose>
            <c:when test="${result.unstable}">
              <div class="alert alert-danger d-flex align-center">
                <i class="icn-danger icn-lg mx-1"></i>
                ${result.summary}
              </div>
            </c:when>
            <c:otherwise>
              <div class="alert alert-success d-flex align-center">
                <i class="icn-success icn-lg mx-1"></i>
                ${result.summary}
              </div>
            </c:otherwise>
          </c:choose>
        </c:when>
        <c:otherwise>
          <c:choose>
            <c:when test="${result.unstable}">
              <div class="alert alert-warning d-flex align-center">
                <i class="icn-warning icn-lg mx-1"></i>
                ${result.summary}
              </div>
            </c:when>
            <c:otherwise>
              <div class="alert alert-success d-flex align-center">
                <i class="icn-success icn-lg mx-1"></i>
                ${result.summary}
              </div>
            </c:otherwise>
          </c:choose>
        </c:otherwise>
      </c:choose>

      <h3>Report Summary</h3>
      <table class="table table-striped w-25 mb-5">
        <thead>
          <tr>
            <th>Gate</th>
            <th>Status</th>
            <c:if test="${result.hasTotal}">
              <th>Errors</th>
            </c:if>
          </tr>
        </thead>
        <tbody>
          <c:if test="${result.checkNewEvents}">
            <c:choose>
              <c:when test="${result.passedNewErrorGate}">
                <tr>
                  <td>
                    <i class="icn-success icn-td"></i>
                    <a href="#new-gate">New</a>
                  </td>
                  <td>Passed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.newGateTotal}</td>
                  </c:if>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>
                    <i class="icn-times icn-td"></i>
                    <a href="#new-gate">New</a>
                  </td>
                  <td>Failed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.newGateTotal}</td>
                  </c:if>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          <c:if test="${result.checkResurfacedEvents}">
            <c:choose>
              <c:when test="${result.passedResurfacedErrorGate}">
                <tr>
                  <td>
                    <i class="icn-success icn-td"></i>
                    <a href="#resurfaced-gate">Resurfaced</a>
                  </td>
                  <td>Passed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.resurfacedGateTotal}</td>
                  </c:if>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>
                    <i class="icn-times icn-td"></i>
                    <a href="#resurfaced-gate">Resurfaced</a>
                  </td>
                  <td>Failed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.resurfacedGateTotal}</td>
                  </c:if>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          <c:if test="${result.checkTotalErrors}">
            <c:choose>
              <c:when test="${result.passedTotalErrorGate}">
                <tr>
                  <td>
                    <i class="icn-success icn-td"></i>
                    <a href="#total-gate">Total</a>
                  </td>
                  <td>Passed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.totalGateTotal}</td>
                  </c:if>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>
                    <i class="icn-times icn-td"></i>
                    <a href="#total-gate">Total</a>
                  </td>
                  <td>Failed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.totalGateTotal}</td>
                  </c:if>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          <c:if test="${result.checkUniqueErrors}">
            <c:choose>
              <c:when test="${result.passedUniqueErrorGate}">
                <tr>
                  <td>
                    <i class="icn-success icn-td"></i>
                    <a href="#unique-gate">Unique</a>
                  </td>
                  <td>Passed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.uniqueGateTotal}</td>
                  </c:if>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>
                    <i class="icn-times icn-td"></i>
                    <a href="#unique-gate">Unique</a>
                  </td>
                  <td>Failed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.uniqueGateTotal}</td>
                  </c:if>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          <c:if test="${result.checkCriticalErrors}">
            <c:choose>
              <c:when test="${result.passedCriticalErrorGate}">
                <tr>
                  <td>
                    <i class="icn-success icn-td"></i>
                    <a href="#critical-gate">Critical</a>
                  </td>
                  <td>Passed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.criticalGateTotal}</td>
                  </c:if>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>
                    <i class="icn-times icn-td"></i>
                    <a href="#critical-gate">Critical</a>
                  </td>
                  <td>Failed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.criticalGateTotal}</td>
                  </c:if>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>
          <c:if test="${result.checkRegressedErrors}">
            <c:choose>
              <c:when test="${result.passedRegressedEvents}">
                <tr>
                  <td>
                    <i class="icn-success icn-td"></i>
                    <a href="#increasing-gate">Increasing</a>
                  </td>
                  <td>Passed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.regressionGateTotal}</td>
                  </c:if>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>
                    <i class="icn-times icn-td"></i>
                    <a href="#increasing-gate">Increasing</a>
                  </td>
                  <td>Failed</td>
                  <c:if test="${result.hasTotal}">
                    <td>${result.regressionGateTotal}</td>
                  </c:if>
                </tr>
              </c:otherwise>
            </c:choose>
          </c:if>

        </tbody>
      </table>

      <c:if test="${result.checkNewEvents}">
        <c:choose>
          <c:when test="${result.passedNewErrorGate}">
            <div class="d-flex align-center mt-4">
              <i class="icn-success mr-1"></i>
              <h4 id="new-gate">${result.newErrorSummary}</h4>
            </div>
            <p class="ml-2 muted">Nothing to report</p>
          </c:when>
          <c:otherwise>
            <div class="d-flex align-center mb-2 mt-4">
              <i class="icn-danger mr-1"></i>
              <h4 id="new-gate">${result.newErrorSummary}</h4>
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
            <div class="d-flex align-center mt-4">
              <i class="icn-success mr-1"></i>
              <h4 id="resurfaced-gate">${result.resurfacedErrorSummary}</h4>
            </div>
            <p class="ml-2 muted">Nothing to report</p>
          </c:when>
          <c:otherwise>
            <div class="d-flex align-center mb-2 mt-4">
              <i class="icn-danger mr-1"></i>
              <h4 id="resurfaced-gate">${result.resurfacedErrorSummary}</h4>
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
                <i class="icn-success mr-1"></i>
                <h4 id="total-gate">${result.totalErrorSummary}</h4>
              </div>
            </c:when>
            <c:otherwise>
              <div class="d-flex align-center mt-4 mb-2">
                <i class="icn-danger mr-1"></i>
                <h4 id="total-gate">${result.totalErrorSummary}</h4>
              </div>
            </c:otherwise>
          </c:choose>
        </c:if>

        <c:if test="${result.checkUniqueErrors}">
          <c:choose>
            <c:when test="${result.passedUniqueErrorGate}">
              <div class="d-flex align-center mt-4 mb-2">
                <i class="icn-success mr-1"></i>
                <h4 id="unique-gate">${result.uniqueErrorSummary}</h4>
              </div>
            </c:when>
            <c:otherwise>
              <div class="d-flex align-center mt-4 mb-2">
                <i class="icn-danger mr-1"></i>
                <h4 id="unique-gate">${result.uniqueErrorSummary}</h4>
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
            <div class="d-flex align-center mt-4">
              <i class="icn-success mr-1"></i>
              <h4 id="critical-gate">${result.criticalErrorSummary}</h4>
            </div>
            <p class="ml-2 muted">Nothing to report</p>
          </c:when>
          <c:otherwise>
            <div class="d-flex align-center mb-2 mt-4">
              <i class="icn-danger mr-1"></i>
              <h4 id="critical-gate">${result.criticalErrorSummary}</h4>
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
            <div class="d-flex align-center mt-4">
              <i class="icn-success mr-1"></i>
              <h4 id="increasing-gate">${result.regressionSumarry}</h4>
            </div>
            <p class="ml-2 muted">Nothing to report</p>
          </c:when>
          <c:otherwise>
            <div class="d-flex align-center mb-2 mt-4">
              <i class="icn-danger mr-1"></i>
              <h4 id="increasing-gate">${result.regressionSumarry}</h4>
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

    </c:otherwise>
  </c:choose>

</section>
