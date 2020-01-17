<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms"%>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="OverOps Query Plugin" className="overopsPlugin">
    <tr>
        <th class="noBorder"><label for="url">OverOpsURL: </label></th>
        <td>
            <props:textProperty id="url" name="url" className="longField"/>
        </td>
    </tr>
    <tr>
        <td class="help-overops" colspan="2">
            <div class="help-text-overops">
                The complete url including port of the OverOps API https://api.overops.com or http://host.domain.com:8080
            </div>
            <div class="help-link-overops">
                (from <a href="https://api.overops.com">OverOps Query Plugin</a>)
            </div>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="envId">OverOps Environment ID: </label></th>
        <td>
            <props:textProperty name="envId" id="envId" className="longField" />
        </td>
    </tr>
    <tr>
        <td class="help-overops" colspan="2">
            <div class="help-text-overops">
                The default OverOps environment identifier (e.g. S12345) if none specified in the build settings. Make sure the "S" is capitalized.
            </div>
            <div class="help-link-overops">
                (from <a href="https://api.overops.com">OverOps Query Plugin</a>)
            </div>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="token">OverOps API Token: </label></th>
        <td>
            <props:textProperty id="token" name="token" className="longField" />
        </td>
    </tr>
    <tr>
        <td class="help-overops" colspan="2">
            <div class="help-text-overops">
                The OverOps REST API token to use for authentication.  This can be obtained from the OverOps dashboard under Settings -> Account.
            </div>
            <div class="help-link-overops">
                (from <a href="https://api.overops.com">OverOps Query Plugin</a>)
            </div>
        </td>
    </tr>
    <tr>
        <td class="help-button" colspan="2">
            <a class="btn cancel" onclick="BS.OOF.testConnection();">Test Connection</a>
        </td>
    </tr>
    <bs:dialog dialogId="testConnectionDialog"
               title="Test Connection"
               closeCommand="BS.TestConnectionDialog.close();"
               closeAttrs="showdiscardchangesmessage='false'">
        <div id="testConnectionStatus">Connection successful!</div>
        <div id="testConnectionDetails" class="mono">Test completed successfully</div>
    </bs:dialog>
</l:settingsGroup>

<l:settingsGroup title="General Settings" className="generalSettings">
    <tr>
        <th class="noBorder"><label for="applicationName">Application Name: </label></th>
        <td>
            <props:textProperty name="applicationName" className="longField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="deploymentName">Deployment Name: </label></th>
        <td>
            <props:textProperty name="deploymentName" className="longField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="regexFilter">Regex Filter: </label></th>
        <td>
            <props:textProperty name="regexFilter" className="longField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="markUnstable">Mark Build Unstable: </label></th>
        <td>
            <props:checkboxProperty name="markUnstable" className="checkBoxField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="printTopIssues">Show Top Issues: </label></th>
        <td>
            <props:textProperty name="printTopIssues" className="longField"/>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Code Quality Gate Options" className="codeQualityOptions">

    <tr>
        <th class="noBorder"><label for="checkNewErrors">
            <props:checkboxProperty  name="checkNewErrors" className="checkBoxField commutator"/> New Error Gate: </label></th>
    </tr>

        <tr class="checkNewErrors" >
            <th class="noBorder"><label for="newEvents">Detect New Errors: </label></th>
            <td>
                <props:checkboxProperty name="newEvents" className="checkBoxField"/>
            </td>
        </tr>


    <tr>
        <th class="noBorder"><label for="checkResurfacedErrors">
            <props:checkboxProperty name="checkResurfacedErrors" className="checkBoxField commutator"/>
            Resurfaced Error Gate: </label></th>
    </tr>
    <tr class="checkResurfacedErrors">
        <th class="noBorder"><label for="resurfacedErrors">Detect Resurfaced Errors: </label></th>
        <td>
            <props:checkboxProperty name="resurfacedErrors" className="checkBoxField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="checkVolumeErrors">
            <props:checkboxProperty name="checkVolumeErrors" className="checkBoxFiel commutator"/>
            Total Error Volume Gate: </label></th>
    </tr>
    <tr class="checkVolumeErrors">
        <th class="noBorder"><label for="maxErrorVolume">Max Allowable Error Volume: </label></th>
        <td>
            <props:textProperty name="maxErrorVolume" className="longField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="checkUniqueErrors">
            <props:checkboxProperty name="checkUniqueErrors" className="checkBoxField commutator"/>
            Unique Error Volume Gate: </label></th>
    </tr>
    <tr class="checkUniqueErrors">
        <th class="noBorder"><label for="maxUniqueErrors">Max Allowable Unique Error Count: </label></th>
        <td>
            <props:textProperty name="maxUniqueErrors" className="longField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="checkCriticalErrors">
            <props:checkboxProperty name="checkCriticalErrors" className="checkBoxField commutator"/>
            Critical Exception Type(s) Gate: </label></th>
    </tr>
    <tr class="checkCriticalErrors">
        <th class="noBorder"><label for="criticalExceptionTypes">Detect Critical Exception Types: </label></th>
        <td>
            <props:textProperty name="criticalExceptionTypes" className="longField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="checkRegressionErrors">
            <props:checkboxProperty name="checkRegressionErrors" className="checkBoxField commutator"/>
            Increasing Errors Gate: </label></th>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="activeTimespan">Active Time Window: </label></th>
        <td>
            <props:textProperty name="activeTimespan" className="longField"/>
        </td>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="baselineTimespan">Baseline Time Window: </label></th>
        <td>
            <props:textProperty name="baselineTimespan" className="longField"/>
        </td>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="minVolumeThreshold">Error Volume Threshold: </label></th>
        <td>
            <props:textProperty name="minVolumeThreshold" className="longField"/>
        </td>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="minErrorRateThreshold">Error Rate Threshold (0-1): </label></th>
        <td>
            <props:textProperty name="minErrorRateThreshold" className="longField"/>
        </td>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="regressionDelta">Regression Delta (0-1): </label></th>
        <td>
            <props:textProperty name="regressionDelta" className="longField"/>
        </td>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="criticalRegressionDelta">Critical Regression Threshold (0-1): </label></th>
        <td>
            <props:textProperty name="criticalRegressionDelta" className="longField"/>
        </td>
    </tr>
    <tr class="checkRegressionErrors">
        <th class="noBorder"><label for="applySeasonality">Apply Seasonality: </label></th>
        <td>
            <props:checkboxProperty name="applySeasonality" className=""/>
        </td>
    </tr>
</l:settingsGroup>
<l:settingsGroup title="Debugging Options" className="codeQualityOptions">
    <tr>
        <th class="noBorder"><label for="debug">Debug Mode: </label></th>
        <td>
            <props:checkboxProperty name="debug" className="checkBoxField"/>
        </td>
    </tr>
</l:settingsGroup>
<style>
    .error_setting {
        border: 2px solid red !important;
    }
</style>
<script type="text/javascript">
    BS.OOF = {
        getProjectId: function() {
            var arr = BS.Navigation.items;
            if (arr != null && arr.length > 2) {
                var projectId = arr[arr.length - 2].projectId;
                if (projectId != null) {
                    return projectId;
                }
            }

            return "noProjectFound";
        },

        save : function() {
            var url = document.getElementById("url").value;
            var token = document.getElementById("token").value;
            var envId = document.getElementById("envId").value;
            var projectId = this.getProjectId();

            if (this.validation(url, token, envId)) {
                BS.ajaxRequest(window['base_uri'] + '/admin/manageOverOps.html', {
                    parameters: Object.toQueryString({
                        "testing" : false,
                        "overops.url": url,
                        "overops.env.id": envId,
                        "overops.token" : token,
                        "projectId": projectId
                    }),
                    onComplete: function(response) {
                        var classFail = "testConnectionFailed";
                        var classOk = "testConnectionSuccess";
                        var text = "Problem with saving settings.";
                        if (response['responseXML'].childNodes[0].getAttribute('status') === "OK") {
                            text = "Setting saved successfully.";
                            jQuery("#resultDialogTitle").removeClass(classFail);
                            jQuery("#resultDialogTitle").addClass(classOk);
                        } else {
                            jQuery("#resultDialogTitle").removeClass(classOk);
                            jQuery("#resultDialogTitle").addClass(classFail);
                        }
                        jQuery("#resultDialogTitle").text(text);
                        jQuery("#resultDialog").show();
                    }
                });
            }
        },

        testConnection: function() {
            var url = document.getElementById("url").value;
            var token = document.getElementById("token").value;
            var envId = document.getElementById("envId").value;
            var projectId = this.getProjectId();

            if (this.validation(url, token, envId)) {
                BS.ajaxRequest(window['base_uri'] + '/admin/manageOverOps.html', {
                    parameters: Object.toQueryString({
                        "testing" : true,
                        "overops.url": url,
                        "overops.env.id": envId,
                        "overops.token" : token,
                        "projectId": projectId
                    }),
                    onComplete: function(response) {
                        var status = response['responseXML'].childNodes[0].getAttribute('status') === "OK";
                        var text = response['responseXML'].childNodes[0].getAttribute('message');
                        BS.TestConnectionDialog.show(status, text, $('testConnection'));
                    }
                });
            }
        },

        validation : function(url, token, envId) {
            var valid = true;
            if (token === null || token === undefined || token === "") {
                valid = false;
                document.getElementById("token").classList.add("error_setting");
            } else {
                document.getElementById("token").classList.remove("error_setting");
            }
            if (url === null || url === undefined || url === "") {
                valid = false;
                document.getElementById("url").classList.add("error_setting");
            } else {
                document.getElementById("url").classList.remove("error_setting");
            }
            if (envId === null || envId === undefined || envId === "") {
                valid = false;
                document.getElementById("envId").classList.add("error_setting");
            } else {
                document.getElementById("envId").classList.remove("error_setting");
            }
            return valid;
        }
    }
</script>
