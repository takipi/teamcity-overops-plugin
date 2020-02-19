<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="unstable" type="java.lang.boolean"--%>

<c:if test="${not empty unstable}">
    <tr>
        <td class="st labels">OverOps:</td>
        <td class="st oo-qr">
            <c:choose>
                <c:when test="${unstable}">
                    <span class="svg-icon js_buildStatusIcon buildStatusIcon buildStatusIcon_error buildStatusIcon_size_S">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
                            <path d="M13 3.05A7 7 0 1013 13a7 7 0 000-9.95zM8.85 12.8h-1.7v-1.71h1.7zm0-3.56H7.17L7 3.2h2z"></path>
                        </svg>
                    </span>
                    Failed
                </c:when>
                <c:otherwise>
                    <span class="svg-icon js_buildStatusIcon buildStatusIcon buildStatusIcon_successful buildStatusIcon_size_S">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
                            <path d="M13 3.05A7 7 0 1013 13a7 7 0 000-9.95zM12 12a5.6 5.6 0 01-8 0 5.61 5.61 0 010-8 5.6 5.6 0 018 0c.1.1.18.22.27.33L7.6 9 5 6.19 3.69 7.4l3.85 4.21 5.66-5.67A5.62 5.62 0 0112 12z"></path>
                        </svg>
                    </span>
                    Passed
                </c:otherwise>
            </c:choose>
        </td>
        <td class="st labels"></td>
        <td class="st fixed"></td>
    </tr>
</c:if>
