<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms"%>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="OverOps API Settings" className="overopsPlugin">
  <tr>
    <th class="noBorder">
      <label for="url">API URL:</label>
    </th>
    <td>
      <props:textProperty id="url" name="url" className="longField" />
      <span class="smallNote">
        The complete URL of the OverOps API including protocal and port (https://api.overops.com or http://host.domain.com:8080)
      </span>
    </td>
  </tr>
  <tr>
    <th class="noBorder">
      <label for="envId">Environment ID:</label>
    </th>
    <td>
      <props:textProperty name="envId" id="envId" className="longField" />
      <span class="smallNote">
        The OverOps environment identifier (e.g. S12345)
      </span>
    </td>
  </tr>
  <tr>
    <th class="noBorder">
      <label for="token">API Token:</label>
    </th>
    <td>
      <props:textProperty id="token" name="token" className="longField" />
      <span class="smallNote">
        The OverOps REST API token to use for authentication. This can be obtained from the OverOps dashboard under Settings -> Account.
      </span>
    </td>
  </tr>
  <tr>
    <th class="noBorder"></th>
    <td>
      <button type="button" class="btn" id="test-connection">Test Connection</button>
      <bs:dialog dialogId="testConnectionDialog"
        title="Test Connection"
        closeCommand="BS.TestConnectionDialog.close();"
        closeAttrs="showdiscardchangesmessage='false'">
        <div id="testConnectionStatus">Connection successful!</div>
        <div id="testConnectionDetails" class="mono">Test completed successfully</div>
      </bs:dialog>
    </td>
  </tr>
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

<l:settingsGroup title="Quality Gate Settings" className="codeQualityOptions">

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
<l:settingsGroup title="Advanced Settings" className="codeQualityOptions">
    <tr>
        <th class="noBorder"><label for="debug">Debug Mode: </label></th>
        <td>
            <props:checkboxProperty name="debug" className="checkBoxField"/>
        </td>
    </tr>
    <tr>
        <th class="noBorder"><label for="errorSuccess">Mark build "success" if unable to generate a Quality Report:</label></th>
        <td>
            <props:checkboxProperty name="errorSuccess" className="checkBoxField"/>
        </td>
    </tr>
</l:settingsGroup>
<style>
  input.errorField { border: 2px solid #c22731; }
</style>
<script type="text/javascript">
  (() => {
    document.getElementById('test-connection').addEventListener('click', (event) => {
      testConnection();
    });
  })();

    function validation(url, token, envId) {
        var valid = true;
        if (token === null || token === undefined || token === "") {
            valid = false;
            document.getElementById("token").classList.add("errorField")
            document.getElementById("token").focus()
        } else {
            document.getElementById("token").classList.remove("errorField")
        }
        if (envId === null || envId === undefined || envId === "") {
            valid = false;
            document.getElementById("envId").classList.add("errorField")
            document.getElementById("envId").focus()
        } else {
            document.getElementById("envId").classList.remove("errorField")
        }
        if (url === null || url === undefined || url === "") {
            valid = false;
            document.getElementById("url").classList.add("errorField")
            document.getElementById("url").focus()
        } else {
            document.getElementById("url").classList.remove("errorField")
        }
        return valid;
    }
    function testConnection() {

        var url = document.getElementById("url").value;
        var token = document.getElementById("token").value;
        var envId = document.getElementById("envId").value;


        if (validation(url, token, envId)) {
            BS.ajaxRequest(window['base_uri'] + '/admin/manageOverOps.html', {
                parameters: Object.toQueryString({
                    "testing" : true,
                    "overops.url": url,
                    "overops.env.id": envId,
                    "overops.token" : token
                }),
                onComplete: function(response) {
                    var status = response['responseXML'].childNodes[0].getAttribute('status') === "OK";
                    var text = response['responseXML'].childNodes[0].getAttribute('message')
                    BS.TestConnectionDialog.show(status, text, $('testConnection'));
                }
            });
        }
    }
    jQuery(function () {
        jQuery('tr').find('.commutator').each(function(){
            if(!jQuery(this).is(':checked')) {
                var id = jQuery(this).attr('id');
                jQuery("."+id).find('.checkBoxField').each(function(){this.setValue(false)});
                jQuery("."+id).find('.longField').each(function(){this.setValue("")});
                jQuery("."+id).hide();
            }
        });
        jQuery('.commutator').change(function(){
            var id = jQuery(this).attr('id');
            if(jQuery(this).is(':checked')) {
                jQuery("."+id).find('.checkBoxField').each(function(){this.setValue(true)});
                jQuery("."+id).show();
            } else {
                jQuery("."+id).find('.checkBoxField').each(function(){this.setValue(false)});
                jQuery("."+id).find('.longField').each(function(){this.setValue("")});
                jQuery("."+id).hide();
            }
        });
    })
</script>