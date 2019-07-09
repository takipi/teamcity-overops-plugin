<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="unstable" type="java.lang.boolean"--%>

<c:if test="${unstable}">
    <tr>
        <td>
        </td>
        <td class="st">
            <span style="color:red">OverOps Quality: BUILD is unstable</span>
        </td>
    </tr>
</c:if>