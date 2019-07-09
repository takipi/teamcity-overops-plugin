<%@ page import="jetbrains.buildServer.serverSide.crypt.RSACipher" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="afn" uri="/WEB-INF/functions/authz" %>
<%@ include file="/include-internal.jsp" %>

<div class="general-setting-overops">
    <forms:multipartForm id="serverInfoForm" onsubmit="return false">

        <table class="runnerFormTable">
            <tr>
                <th>OverOps URL<l:star/></th>
                <td>
                    <div><input type="text" id="url" name="overops.url" value="${url}"/></div>
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
                <th>OverOps Environment ID<l:star/></th>
                <td>
                    <div><input type="text" id="envId" name="overops.env.id" value="${envId}"/></div>
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
                <th>OverOps API Token<l:star/></th>
                <td>
                    <div><input type="text" id="token" name="overops.token" value="${token}"/></div>
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
                    <input type="hidden" name="projectId" id="projectId" value="${projectId}"/>
                    <button class="button-save" onclick="save()">Save</button>
                    <button class="button-save" onclick="testConnection()">Test Connection</button>
                </td>
            </tr>
        </table>
        <bs:dialog dialogId="testConnectionDialog"
                   title="Test Connection"
                   closeCommand="BS.TestConnectionDialog.close();"
                   closeAttrs="showdiscardchangesmessage='false'">
            <div id="testConnectionStatus">Connection successful!</div>
            <div id="testConnectionDetails" class="mono">Test completed successfully</div>
        </bs:dialog>
    </forms:multipartForm>

    <div id="resultDialog" class=" modalDialog" style="z-index: 10; top: 50%; left: 40%; position: absolute; display:none"  data-modal="true">
        <div class="dialogHeader">
            <div class="closeWindow">
                <a class="closeWindowLink" title="Close dialog window" href="#" onclick="closePopUp()" >x</a>
            </div>

        </div>
        <div class="modalDialogBody">

            <div id="resultDialogTitle" class=""></div>

        </div>
    </div>
</div>
<script type="text/javascript">
    function closePopUp() {
        jQuery("#resultDialog").hide();
    }
    function save() {
        var url = document.getElementById("url").value;
        var token = document.getElementById("token").value;
        var envId = document.getElementById("envId").value;

        if (validation(url, token, envId)) {
            BS.ajaxRequest(window['base_uri'] + '/admin/manageOverOps.html', {
                parameters: Object.toQueryString({
                    "testing" : false,
                    "overops.url": url,
                    "overops.env.id": envId,
                    "overops.token" : token,
                    "projectId": document.getElementById("projectId").value
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
                    "overops.token" : token,
                    "projectId": document.getElementById("projectId").value
                }),
                onComplete: function(response) {
                    var status = response['responseXML'].childNodes[0].getAttribute('status') === "OK";
                    var text = response['responseXML'].childNodes[0].getAttribute('message')
                    BS.TestConnectionDialog.show(status, text, $('testConnection'));
                }
            });
        }
    }
    function validation(url, token, envId) {
        var valid = true;
        if (token === null || token === undefined || token === "") {
            valid = false;
            document.getElementById("token").classList.add("error_setting")
        } else {
            document.getElementById("token").classList.remove("error_setting")
        }
        if (url === null || url === undefined || url === "") {
            valid = false;
            document.getElementById("url").classList.add("error_setting")
        } else {
            document.getElementById("url").classList.remove("error_setting")
        }
        if (envId === null || envId === undefined || envId === "") {
            valid = false;
            document.getElementById("envId").classList.add("error_setting")
        } else {
            document.getElementById("envId").classList.remove("error_setting")
        }
        return valid;
    }
</script>