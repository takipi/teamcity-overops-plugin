<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="report" type="java.lang.boolean"--%>
<%--@elvariable id="unstable" type="java.lang.boolean"--%>
<%--@elvariable id="overOpsMsg" type="java.lang.String"--%>

<c:if test="${unstable}">
    <tr>
        <td>
        </td>
        <td class="st">
            <span style="color:red">OverOps analytics: BUILD is unstable</span>
        </td>
    </tr>
</c:if>

<tr>
    <td>
    </td>
    <td class="st">
        <c:if test="${report}">
            <a href="">View in OverOps</a>
        </c:if>
        <c:if test="${!report}">
            <span >${overOpsMsg}</span>
        </c:if>
    </td>
</tr>