<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>


<%@ attribute name="id" required="true" %>
<%@ attribute name="days" required="false" %>
<%@ attribute name="appointment_id" required="true" %>
<%@ attribute name="status" required="true" %>
<%@ attribute name="redirect" required="false" %>

<head>
    <title>Change status of appointment</title>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="localization.messages"/>
    <script type="text/javascript" src="/static/scripts.js"></script>
</head>
<body>

<c:choose>
    <c:when test="${status == 'NOT_PAID'}">
        <form onsubmit="if(!confirm('<fmt:message key="submit.sure"/>')){return false;}"
              action="${pageContext.request.contextPath}/salon"
              method="post">
            <input hidden name="command" value="edit-appointment">
            <input hidden name="id" value="${id}">
            <input hidden name="redirect" value="${redirect}">
            <input hidden name="days" value="${days}">
            <input hidden name="appointment_id" value="${appointment_id}">
            <button type="submit" name="payment_status" value="PAID_IN_SALON" class="btn btn-info">
                <fmt:message key="change.paid"/>
            </button>
        </form>
    </c:when>
</c:choose>
</body>

