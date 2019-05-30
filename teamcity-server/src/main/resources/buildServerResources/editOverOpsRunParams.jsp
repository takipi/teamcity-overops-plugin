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
            <props:textProperty name="url" className="longField" value="${url}"/>
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
            <props:textProperty name="envId" className="longField" value="${envId}" />
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
            <props:textProperty name="token" className="longField" value="${token}"/>
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
        <th class="noBorder"><label for="serviceId">Environment ID: </label></th>
        <td>
            <props:textProperty name="serviceId" className="longField"/>
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
        <th class="noBorder"><label for="printTopIssues">Show Top Issue: </label></th>
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
<script type="text/javascript">
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